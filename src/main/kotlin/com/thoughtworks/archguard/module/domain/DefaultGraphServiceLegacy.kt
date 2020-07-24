package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.model.*
import org.slf4j.LoggerFactory

abstract class DefaultGraphServiceLegacy(val logicModuleRepository: LogicModuleRepository, val dependencyService: DependencyService) : GraphServiceLegacy {
    private val log = LoggerFactory.getLogger(DefaultGraphServiceLegacy::class.java)

    override fun getLogicModuleGraphLegacy(): ModuleGraph {
        val moduleDependencies = getModuleDependency()

        val moduleStore: ModuleStoreLegacy = ModuleStoreLegacy()
        moduleDependencies
                .groupBy { it.caller }
                .forEach {
                    it.value.groupBy { i -> i.callee }
                            .forEach { i -> moduleStore.addEdge(it.key.name, i.key.name, i.value.size) }
                }

        return moduleStore.getModuleGraph()
    }

    override fun getLogicModuleGraph(): Graph<LogicModule> {
        val moduleDependencies = getModuleDependency()

        val moduleStore = GraphStore<LogicModule>()
        moduleDependencies
                .groupBy { it.caller }
                .forEach {
                    it.value.groupBy { i -> i.callee }
                            .forEach { i -> moduleStore.addEdge(it.key, i.key, i.value.size) }
                }

        return moduleStore.getGraph()
    }

    private fun getModuleDependency(): List<Dependency<LogicModule>> {
        val modules = logicModuleRepository.getAllByShowStatus(true)
        val members = modules.map { it.members }.flatten()
        val dependencies = dependencyService.getAllClassDependencyLegacy(members)
        return mapClassDependenciesToModuleDependencies(dependencies, modules)
    }

    fun mapClassDependenciesToModuleDependencies(classDependency: List<Dependency<JClassVO>>, logicModules: List<LogicModule>): List<Dependency<LogicModule>> {
        // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
        return classDependency.map { mapClassDependencyToModuleDependency(logicModules, it) }.flatten()
                .filter { it.caller != it.callee }
    }

    abstract fun mapClassDependencyToModuleDependency(logicModules: List<LogicModule>, jClassDependency: Dependency<JClassVO>): List<Dependency<LogicModule>>

    fun mapModuleDependencyToServiceDependency(moduleDependencies: List<Dependency<LogicModule>>): List<Dependency<LogicModule>> {
        val logicModules = logicModuleRepository.getAll()
        val servicesDependencies = mutableListOf<Dependency<LogicModule>>()
        for (it in moduleDependencies) {
            val callerModule = it.caller
            val calleeModule = it.callee
            val callerServices = logicModules.filter { it.containsOrEquals(callerModule) }.toMutableList()
            if (callerServices.isEmpty()) {
                callerServices.add(callerModule)
            }
            val calleeServices = logicModules.filter { it.containsOrEquals(calleeModule) }.toMutableList()
            if (calleeServices.isEmpty()) {
                calleeServices.add(calleeModule)
            }
            val flatMap = callerServices.flatMap { lhsElem -> calleeServices.map { rhsElem -> Dependency(lhsElem, rhsElem) } }
            servicesDependencies.addAll(flatMap)
        }
        return servicesDependencies
    }
}

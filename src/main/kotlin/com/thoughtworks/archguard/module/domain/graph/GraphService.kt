package com.thoughtworks.archguard.module.domain.graph

import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.*
import com.thoughtworks.archguard.module.domain.plugin.PluginManager
import org.springframework.stereotype.Service

@Service
class GraphService(val logicModuleRepository: LogicModuleRepository, val dependencyService: DependencyService, val pluginManager: PluginManager) {

    fun getLogicModuleGraph(): Graph {
        val moduleDependencies = getModuleDependency()

        val moduleStore = GraphStore()

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
        val dependencies = dependencyService.getAllClassDependencies()
        return mapMethodDependenciesToModuleDependencies(dependencies, modules)
    }

    private fun mapMethodDependenciesToModuleDependencies(dependencies: List<Dependency<JClassVO>>, logicModules: List<LogicModule>): List<Dependency<LogicModule>> {
        // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
        val logicModuleDependencies = dependencies.flatMap { mapMethodDependencyToModuleDependency(it, logicModules) }
        return logicModuleDependencies.filter { it.caller != it.callee }
    }

    private fun mapMethodDependencyToModuleDependency(methodDependency: Dependency<JClassVO>, logicModules: List<LogicModule>): List<Dependency<LogicModule>> {
        val callerModules = getModule(logicModules, methodDependency.caller)
        val calleeModules = getModule(logicModules, methodDependency.callee)

        return callerModules.flatMap { caller -> calleeModules.map { callee -> Dependency(caller, callee) } }
    }

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

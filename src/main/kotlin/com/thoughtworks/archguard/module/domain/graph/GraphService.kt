package com.thoughtworks.archguard.module.domain.graph

import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.*
import org.springframework.stereotype.Service

@Service
class GraphService(val logicModuleRepository: LogicModuleRepository, val dependencyService: DependencyService) {

    fun getLogicModuleGraph(): Graph<LogicModule> {
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
        val dependencies = dependencyService.getAll()
        return mapMethodDependenciesToModuleDependencies(dependencies, modules)
    }

    private fun mapMethodDependenciesToModuleDependencies(classDependency: List<Dependency<JMethodVO>>, logicModules: List<LogicModule>): List<Dependency<LogicModule>> {
        // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
        return classDependency.map { mapMethodDependencyToModuleDependency(it, logicModules) }.flatten()
                .filter { it.caller != it.callee }
    }

    private fun mapMethodDependencyToModuleDependency(methodDependency: Dependency<JMethodVO>, logicModules: List<LogicModule>): List<Dependency<LogicModule>> {
        val callerModules = getModule(logicModules, methodDependency.caller.jClassVO)
        val calleeModules = getModule(logicModules, methodDependency.callee.jClassVO)

        return callerModules.flatMap { caller -> calleeModules.map { callee -> Dependency(caller, callee) } }

    }

}

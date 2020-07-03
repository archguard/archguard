package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.ModuleGraph
import com.thoughtworks.archguard.module.domain.model.ModuleStore
import org.slf4j.LoggerFactory

abstract class DefaultGraphService(var logicModuleRepository: LogicModuleRepository) : GraphService {
    private val log = LoggerFactory.getLogger(DefaultGraphService::class.java)

    override fun getLogicModuleGraph(): ModuleGraph {
        val moduleDependencies = getModuleDependency()

        val moduleStore: ModuleStore = ModuleStore()
        moduleDependencies
                .groupBy { it.caller }
                .forEach {
                    it.value.groupBy { i -> i.callee }
                            .forEach { i -> moduleStore.addEdge(it.key.name, i.key.name, i.value.size) }
                }

        return moduleStore.getModuleGraph()
    }

    private fun getModuleDependency(): List<Dependency<LogicModule>> {
        val modules = logicModuleRepository.getAllByShowStatus(true)
        val members = modules.map { it.members }.flatten()
        val classDependencies = logicModuleRepository.getAllClassDependency(members)
        return mapClassDependenciesToModuleDependencies(classDependencies, modules)
    }

    fun mapClassDependenciesToModuleDependencies(classDependency: List<Dependency<JClass>>, logicModules: List<LogicModule>): List<Dependency<LogicModule>> {
        // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
        return classDependency.map { mapClassDependencyToModuleDependency(logicModules, it) }.flatten()
                .filter { it.caller != it.callee }
    }

    abstract fun mapClassDependencyToModuleDependency(logicModules: List<LogicModule>, jClassDependency: Dependency<JClass>): List<Dependency<LogicModule>>
}
package com.thoughtworks.archguard.module.domain

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

    private fun getModuleDependency(): List<NewDependency<NewLogicModule>> {
        val modules = logicModuleRepository.getAllByShowStatusNew(true)
        val members = modules.map { it.members }.flatten()
        val classDependencies = logicModuleRepository.getAllClassDependencyNew(members)
        return mapClassDependenciesToModuleDependencies(classDependencies, modules)
    }

    fun mapClassDependenciesToModuleDependencies(classDependency: List<NewDependency<JClass>>, logicModules: List<NewLogicModule>): List<NewDependency<NewLogicModule>> {
        // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
        return classDependency.map { mapClassDependencyToModuleDependency(logicModules, it) }.flatten()
                .filter { it.caller != it.callee }
    }

    abstract fun mapClassDependencyToModuleDependency(logicModules: List<NewLogicModule>, jClassDependency: NewDependency<JClass>): List<NewDependency<NewLogicModule>>
}
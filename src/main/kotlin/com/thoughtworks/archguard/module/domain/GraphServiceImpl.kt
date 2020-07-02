package com.thoughtworks.archguard.module.domain

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class GraphServiceImpl : GraphService {
    private val log = LoggerFactory.getLogger(GraphServiceImpl::class.java)

    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

    var dependencyAnalysisHelper: DependencyAnalysisHelper

    constructor(@Qualifier("Default") dependencyAnalysisHelper: DependencyAnalysisHelper) {
        this.dependencyAnalysisHelper = dependencyAnalysisHelper
    }


    override fun getLogicModuleGraph(): ModuleGraph {
        val moduleDependencies = getModuleDependency()

        val moduleStore = ModuleStore()
        moduleDependencies
                .groupBy { it.caller }
                .forEach {
                    it.value.groupBy { i -> i.callee }
                            .forEach { i -> moduleStore.addEdge(it.key, i.key, i.value.size) }
                }

        return moduleStore.getModuleGraph()
    }

    private fun getModuleDependency(): List<Dependency> {
        val modules = logicModuleRepository.getAllByShowStatus(true)
        val members = modules.map { it.members }.flatten()
        val classDependencies = logicModuleRepository.getAllClassDependency(members)
        return mapClassDependenciesToModuleDependencies(classDependencies, modules)
    }

    fun mapClassDependenciesToModuleDependencies(classDependency: List<Dependency>, logicModules: List<LogicModule>): List<Dependency> {
        // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
        return classDependency.map { mapClassDependencyToModuleDependency(logicModules, it) }.flatten()
                .filter { it.caller != it.callee }
    }

    private fun mapClassDependencyToModuleDependency(logicModules: List<LogicModule>, classDependency: Dependency): List<Dependency> {
        val callerModules = getModule(logicModules, classDependency.caller)
        if (callerModules.size > 1) {
            log.error("Caller Class belong to more than one Module!", callerModules)
        }
        val callerModule = callerModules[0]
        val calleeModules = getModule(logicModules, classDependency.callee)

        val calleeModulesByXml = dependencyAnalysisHelper.analysis(classDependency, logicModules, calleeModules)

        return calleeModulesByXml.map { Dependency(callerModule, it) }
    }
}
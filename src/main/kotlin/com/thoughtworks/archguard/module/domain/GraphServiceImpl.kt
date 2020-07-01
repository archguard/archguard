package com.thoughtworks.archguard.module.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GraphServiceImpl : GraphService {
    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

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
        return mapClassDependencyToModuleDependency(classDependencies, modules)
    }
}

fun mapClassDependencyToModuleDependency(classDependency: List<Dependency>, logicModules: List<LogicModule>): List<Dependency> {
    // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
    return classDependency.map {
        val callerModules = getModule(logicModules, it.caller)
        val calleeModules = getModule(logicModules, it.callee)
        callerModules.flatMap { callerModule -> calleeModules.map { calleeModule -> callerModule to calleeModule } }
                .map { it -> Dependency(it.first, it.second) }
    }.flatten().filter { it.caller != it.callee }
}
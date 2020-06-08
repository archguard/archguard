package com.thoughtworks.archguard.dependence.domain.logic_module

import com.thoughtworks.archguard.dependence.domain.base_module.BaseModuleRepository
import com.thoughtworks.archguard.dependence.domain.base_module.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LogicModuleService {
    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

    @Autowired
    lateinit var baseModuleRepository: BaseModuleRepository

    fun getLogicModules(): List<LogicModule> {
        return logicModuleRepository.getAll()
    }

    fun updateLogicModule(id: String, logicModule: LogicModule) {
        logicModuleRepository.update(id, logicModule)
    }

    fun createLogicModule(logicModule: LogicModule): String {
        val id = UUID.randomUUID().toString()
        logicModule.id = id
        logicModuleRepository.create(logicModule)
        return id
    }

    fun deleteLogicModule(id: String) {
        logicModuleRepository.delete(id)
    }

    fun getLogicModulesDependencies(caller: String, callee: String): List<ModuleDependency> {
        return logicModuleRepository.getDependence(caller, callee)
    }

    fun autoDefineLogicModule() {
        logicModuleRepository.deleteAll()
        val defaultModules = baseModuleRepository.getBaseModules()
                .map { LogicModule(UUID.randomUUID().toString(), it, mutableListOf(it)) }
        logicModuleRepository.saveAll(defaultModules)
    }

    fun autoDefineLogicModuleWithInterface() {
        logicModuleRepository.deleteAll()
        val jClassesHasModules: List<JClass> = baseModuleRepository.getJClassesHasModules()
        val defineLogicModuleWithInterface = getLogicModulesForAllJClass(jClassesHasModules)
        logicModuleRepository.saveAll(defineLogicModuleWithInterface)
    }

    fun getLogicModulesForAllJClass(jClassesHasModules: List<JClass>): List<LogicModule> {
        return jClassesHasModules
                .map { getLogicModuleForJClass(it) }
                .groupBy({ it.name }, { it.members })
                .mapValues { entry -> entry.value.flatten().toSet().toList() }
                .map { LogicModule(UUID.randomUUID().toString(), it.key, it.value) }
    }

    fun getLogicModuleForJClass(jClass: JClass): LogicModule {
        val (id, _, moduleName) = jClass
        val parentClassIds = logicModuleRepository.getParentClassId(id)
        val moduleNames = parentClassIds.map { id -> baseModuleRepository.getJClassesById(id) }
                .map { j -> j.module + "." + j.name }
                .toSet().toMutableList()
        moduleNames.add(moduleName)
        return LogicModule(null, moduleName, moduleNames)
    }

    fun getLogicModuleGraph(): ModuleGraph {
        val results = getModuleDependency()

        val moduleStore = ModuleStore()
        results
                .groupBy { it.caller }
                .forEach {
                    it.value.groupBy { i -> i.callee }
                            .forEach { i -> moduleStore.addEdge(it.key, i.key, i.value.size) }
                }

        return moduleStore.getModuleGraph()
    }

    private fun getModuleDependency(): List<ModuleGraphDependency> {
        val modules = logicModuleRepository.getAll()
        val members = modules.map { it.members }.flatten()
        val results = logicModuleRepository.getAllDependence(members)
        return mapToModule(results, modules)
    }

    private fun mapToModule(results: List<ModuleGraphDependency>, modules: List<LogicModule>): List<ModuleGraphDependency> {
        // TODO[如果module配置时有重叠部分怎么办]
        return results.map {
            val caller = modules.filter { logicModule -> logicModule.members.any { j -> it.caller.startsWith(j) } }
                    .map { logicModule -> logicModule.name }[0]
            val callee = modules.filter { logicModule -> logicModule.members.any { j -> it.callee.startsWith(j) } }
                    .map { logicModule -> logicModule.name }[0]
            ModuleGraphDependency(caller, callee)
        }.filter { it.caller != it.callee }
    }

    fun getLogicModuleCoupling(): List<ModuleCouplingReport> {
        val moduleDependency = getModuleDependency()
        return getLogicModules().map { getModuleCouplingReport(it, moduleDependency) }
    }

    fun getModuleCouplingReport(module: LogicModule,
                                moduleDependency: List<ModuleGraphDependency>): ModuleCouplingReport {
        val fanOut = moduleDependency.filter { it.caller == module.name }.count()
        val fanIn = moduleDependency.filter { it.callee == module.name }.count()
        return ModuleCouplingReport(module.name, fanIn, fanOut)
    }

}

data class ModuleCouplingReport(val module: String,
                                val fanIn: Int,
                                val fanOut: Int) {
    val moduleInstability: Double = if (fanIn + fanOut == 0) 0.0 else fanOut.toDouble() / (fanOut + fanIn)
    val moduleCoupling: Double = if (fanIn + fanOut == 0) 0.0 else 1-1.0 / (fanOut+fanIn)

    constructor() : this("", 0, 0)
}

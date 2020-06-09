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
        val moduleNames = parentClassIds.asSequence().map { id -> baseModuleRepository.getJClassesById(id) }
                .filter { j -> j.module != "null" }
                .filter { j -> j.module != jClass.module }
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
        // TODO[一个Service有多个实现？]
        return results.map {
            val caller = getClassModule(modules, it.caller)
            val callee = getClassModule(modules, it.callee)
            ModuleGraphDependency(caller, callee)
        }.filter { it.caller != it.callee }
    }

    fun getClassModule(modules: List<LogicModule>, className: String): String {
        val callerByFullMatch = fullMatch(className, modules)
        if (callerByFullMatch != null) {
            return callerByFullMatch
        }
        return startsWithMatch(className, modules)
    }

    private fun fullMatch(className: String, modules: List<LogicModule>): String? {
        val fullMatchModule = modules.firstOrNull { logicModule ->
            logicModule.members.any { javaClass -> className == javaClass }
        }
        return fullMatchModule?.name
    }

    private fun startsWithMatch(callerClass: String, modules: List<LogicModule>): String {
        val fullMatchModule = modules.firstOrNull { logicModule ->
            logicModule.members.any { member -> callerClass.startsWith(member) }
        } ?: throw RuntimeException("No LogicModule matched!")
        return fullMatchModule.name
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

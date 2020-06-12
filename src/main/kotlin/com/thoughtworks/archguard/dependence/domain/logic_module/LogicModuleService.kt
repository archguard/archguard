package com.thoughtworks.archguard.dependence.domain.logic_module

import com.fasterxml.jackson.annotation.JsonIgnore
import com.thoughtworks.archguard.dependence.domain.base_module.BaseModuleRepository
import com.thoughtworks.archguard.dependence.domain.base_module.JClass
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LogicModuleService {
    private val log = LoggerFactory.getLogger(LogicModuleService::class.java)

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

    fun mapToModule(results: List<ModuleGraphDependency>, modules: List<LogicModule>): List<ModuleGraphDependency> {
        // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
        return results.map {
            val caller = getClassModule(modules, it.caller)
            val callee = getClassModule(modules, it.callee)
            caller.flatMap { callerElem -> callee.map { calleeElem -> callerElem to calleeElem } }
                    .map { it -> ModuleGraphDependency(it.first, it.second) }
        }.flatten().filter { it.caller != it.callee }
    }

    fun getClassModule(modules: List<LogicModule>, className: String): List<String> {
        val callerByFullMatch = fullMatch(className, modules)
        if (callerByFullMatch.isNotEmpty()) {
            log.info("Class is {} match module {} by fullMatch", className, callerByFullMatch)
            return callerByFullMatch
        }
        val startsWithMatch = startsWithMatch(className, modules)
        log.info("Class is {} match module {} by startsWithMatch", className, startsWithMatch)
        return startsWithMatch
    }

    private fun fullMatch(className: String, modules: List<LogicModule>): List<String> {
        return modules.filter { logicModule ->
            logicModule.members.any { javaClass -> className == javaClass }
        }.map { it.name }
    }

    private fun startsWithMatch(callerClass: String, modules: List<LogicModule>): List<String> {
        var maxMatchSize = 0
        val matchModule: MutableList<String> = mutableListOf()
        for (logicModule in modules) {
            val maxMatchSizeInLogicModule = logicModule.members
                    .filter { member -> callerClass.startsWith(member + ".") }
                    .maxBy { it.length }
                    ?: continue
            if (maxMatchSizeInLogicModule.length > maxMatchSize) {
                maxMatchSize = maxMatchSizeInLogicModule.length
                matchModule.clear()
                matchModule.add(logicModule.name)
            } else if (maxMatchSizeInLogicModule.length == maxMatchSize) {
                matchModule.add(logicModule.name)
            }
        }
        if (matchModule.isEmpty()) {
            throw RuntimeException("No LogicModule matched!")
        }
        return matchModule.toList()
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

    fun getLogicModuleCouplingByClass(): List<NewModuleCouplingReport> {
        val modules = logicModuleRepository.getAll()
        val members = modules.map { it.members }.flatten()
        val dependency = logicModuleRepository.getAllDependence(members)
        return dependency.flatMap { listOf(it.callee, it.caller) }.distinct()
                .map { getClassCouplingReport(it, dependency) }
                .groupBy { getClassModule(modules, it.clazz)[0] }
                .map { NewModuleCouplingReport(it.key, it.value) }
    }

    fun getClassCouplingReport(clazz: String,
                               dependency: List<ModuleGraphDependency>): ClassCouplingReport {
        val fanIn = dependency.filter { it.callee == clazz }.count()
        val fanOut = dependency.filter { it.caller == clazz }.count()
        return ClassCouplingReport(clazz, fanIn, fanOut)
    }

}

data class ModuleCouplingReport(val module: String,
                                val fanIn: Int,
                                val fanOut: Int) {
    val moduleInstability: Double = if (fanIn + fanOut == 0) 0.0 else fanOut.toDouble() / (fanOut + fanIn)
    val moduleCoupling: Double = if (fanIn + fanOut == 0) 0.0 else 1 - 1.0 / (fanOut + fanIn)

    constructor() : this("", 0, 0)
}

data class NewModuleCouplingReport(val module: String,
                                   @JsonIgnore val classCouplingReports: List<ClassCouplingReport>) {
    val moduleInstability: Double = classCouplingReports.map { it.moduleInstability }.average()
    val moduleCoupling: Double = classCouplingReports.map { it.moduleCoupling }.average()
}

data class ClassCouplingReport(val clazz: String,
                               val fanIn: Int,
                               val fanOut: Int) {
    val moduleInstability: Double = if (fanIn + fanOut == 0) 0.0 else fanOut.toDouble() / (fanOut + fanIn)
    val moduleCoupling: Double = if (fanIn + fanOut == 0) 0.0 else 1 - 1.0 / (fanOut + fanIn)
}

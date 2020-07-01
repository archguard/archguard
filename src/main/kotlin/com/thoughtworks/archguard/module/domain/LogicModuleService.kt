package com.thoughtworks.archguard.module.domain

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

    @Autowired
    lateinit var xmlConfigService: XmlConfigService

    fun getLogicModules(): List<LogicModule> {
        return logicModuleRepository.getAll()
    }

    fun hideAllLogicModules() {
        val logicModules = getLogicModules()
        logicModules.forEach { it.hide() }
        logicModuleRepository.updateAll(logicModules)
    }

    fun showAllLogicModules() {
        val logicModules = getLogicModules()
        logicModules.forEach { it.show() }
        logicModuleRepository.updateAll(logicModules)
    }

    fun reverseAllLogicModulesStatus() {
        val logicModules = getLogicModules()
        logicModules.forEach { it.reverse() }
        logicModuleRepository.updateAll(logicModules)
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

    internal fun getLogicModulesForAllJClass(jClassesHasModules: List<JClass>): List<LogicModule> {
        return jClassesHasModules
                .map { getIncompleteLogicModuleForJClass(it) }
                .groupBy({ it.name }, { it.members })
                .mapValues { entry -> entry.value.flatten().toSet().toList() }
                .map { LogicModule(UUID.randomUUID().toString(), it.key, it.value) }
    }

    internal fun getIncompleteLogicModuleForJClass(jClass: JClass): LogicModule {
        val (id, _, moduleName) = jClass
        val parentClassIds = logicModuleRepository.getParentClassId(id)
        val membersGeneratedByParentClasses = parentClassIds.asSequence().map { id -> baseModuleRepository.getJClassesById(id) }
                .filter { j -> j.module != "null" }
                .filter { j -> j.module != jClass.module }
                .map { j -> j.module + "." + j.name }
                .toSet().toMutableList()
        membersGeneratedByParentClasses.add(moduleName)
        return LogicModule(null, moduleName, membersGeneratedByParentClasses)
    }
}

fun getModule(modules: List<LogicModule>, name: String): List<String> {
    val callerByFullMatch = fullMatch(name, modules)
    if (callerByFullMatch.isNotEmpty()) {
        return callerByFullMatch
    }
    return startsWithMatch(name, modules)
}

private fun fullMatch(name: String, modules: List<LogicModule>): List<String> {
    return modules.filter { logicModule ->
        logicModule.members.any { javaClass -> name == javaClass }
    }.map { it.name }
}

private fun startsWithMatch(name: String, modules: List<LogicModule>): List<String> {
    var maxMatchSize = 0
    val matchModule: MutableList<String> = mutableListOf()
    for (logicModule in modules) {
        val maxMatchSizeInLogicModule = logicModule.members
                .filter { member -> name.startsWith("$member.") }
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
        throw RuntimeException("$name No LogicModule matched!")
    }
    return matchModule.toList()
}

fun mapClassDependencyToModuleDependency(classDependency: List<Dependency>, modules: List<LogicModule>): List<Dependency> {
    // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
    return classDependency.map {
        val callerModules = getModule(modules, it.caller)
        val calleeModules = getModule(modules, it.callee)
        callerModules.flatMap { callerModule -> calleeModules.map { calleeModule -> callerModule to calleeModule } }
                .map { it -> Dependency(it.first, it.second) }
    }.flatten().filter { it.caller != it.callee }
}
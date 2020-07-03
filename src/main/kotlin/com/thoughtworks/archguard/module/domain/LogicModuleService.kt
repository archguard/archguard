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
    lateinit var jClassRepository: JClassRepository

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

    fun autoDefineLogicModule() {
        logicModuleRepository.deleteAll()
        val defaultModules = baseModuleRepository.getBaseModules()
                .map { NewLogicModule(UUID.randomUUID().toString(), it, mutableListOf(SubModule(it))) }
        logicModuleRepository.saveAll(defaultModules)
    }

    fun autoDefineLogicModuleWithInterface() {
        logicModuleRepository.deleteAll()
        val jClassesHasModules: List<JClass> = baseModuleRepository.getJClassesHasModules()
        val defineLogicModuleWithInterface = getLogicModulesForAllJClass(jClassesHasModules)
        logicModuleRepository.saveAll(defineLogicModuleWithInterface)
    }

    internal fun getLogicModulesForAllJClass(jClassesHasModules: List<JClass>): List<NewLogicModule> {
        return jClassesHasModules
                .map { getIncompleteLogicModuleForJClass(it) }
                .groupBy({ it.name }, { it.members })
                .mapValues { entry -> entry.value.flatten().toSet().toList() }
                .map { NewLogicModule(UUID.randomUUID().toString(), it.key, it.value) }
    }

    // TODO: use interface not parent class
    internal fun getIncompleteLogicModuleForJClass(jClass: JClass): NewLogicModule {
        val id = jClass.id
        val moduleName = jClass.module
        val parentClassIds = logicModuleRepository.getParentClassId(id)
        val membersGeneratedByParentClasses: MutableList<ModuleMember> = parentClassIds.asSequence().map { id -> jClassRepository.getJClassById(id)!! }
                .filter { j -> j.module != "null" }
                .filter { j -> j.module != jClass.module }
                .toSet().toMutableList()
        membersGeneratedByParentClasses.add(SubModule(moduleName))
        return NewLogicModule(null, moduleName, membersGeneratedByParentClasses.toList())
    }
}

fun getModule(modules: List<LogicModule>, name: String): List<String> {
    val callerByFullMatch = fullMatch(name, modules)
    if (callerByFullMatch.isNotEmpty()) {
        return callerByFullMatch
    }
    return startsWithMatch(name, modules)
}

fun getNewModule(modules: List<NewLogicModule>, jClass: ModuleMember): List<NewLogicModule> {
    val callerByFullMatch = fullMatchNew(jClass, modules)
    if (callerByFullMatch.isNotEmpty()) {
        return callerByFullMatch
    }
    return startsWithMatchNew(jClass, modules)
}

private fun fullMatch(name: String, modules: List<LogicModule>): List<String> {
    return modules.filter { logicModule ->
        logicModule.members.any { javaClass -> name == javaClass }
    }.map { it.name }
}

private fun fullMatchNew(jClass: ModuleMember, modules: List<NewLogicModule>): List<NewLogicModule> {
    return modules.filter { logicModule ->
        logicModule.members.any { moduleMember -> jClass.getFullName() == moduleMember.getFullName() }
    }
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

fun startsWithMatchNew(jClass: ModuleMember, modules: List<NewLogicModule>): List<NewLogicModule> {
    var maxMatchSize = 0
    val matchModule: MutableList<NewLogicModule> = mutableListOf()
    for (logicModule in modules) {
        val maxMatchSizeInLogicModule = logicModule.members
                .filter { member -> jClass.getFullName().startsWith("${member.getFullName()}.") }
                .maxBy { it.getFullName().length }
                ?: continue
        if (maxMatchSizeInLogicModule.getFullName().length > maxMatchSize) {
            maxMatchSize = maxMatchSizeInLogicModule.getFullName().length
            matchModule.clear()
            matchModule.add(logicModule)
        } else if (maxMatchSizeInLogicModule.getFullName().length == maxMatchSize) {
            matchModule.add(logicModule)
        }
    }
    if (matchModule.isEmpty()) {
        throw RuntimeException("$jClass No LogicModule matched!")
    }
    return matchModule.toList()
}

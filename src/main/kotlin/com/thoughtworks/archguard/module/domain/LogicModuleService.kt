package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.ModuleMember
import com.thoughtworks.archguard.module.domain.model.SubModule
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
                .map { LogicModule(UUID.randomUUID().toString(), it, mutableListOf(SubModule(it))) }
        logicModuleRepository.saveAll(defaultModules)
    }

    fun autoDefineLogicModuleWithInterface() {
        logicModuleRepository.deleteAll()
        val jClassesHasModules: List<JClass> = jClassRepository.getJClassesHasModules()
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
        val id = jClass.id
        val moduleName = jClass.module
        val parentClassIds = logicModuleRepository.getParentClassId(id!!)
        val membersGeneratedByParentClasses: MutableList<ModuleMember> = parentClassIds.asSequence().map { id -> jClassRepository.getJClassById(id)!! }
                .filter { j -> j.module != "null" }
                .filter { j -> j.module != jClass.module }
                .filter { j -> j.isInterface() }
                .toSet().toMutableList()
        membersGeneratedByParentClasses.add(SubModule(moduleName))
        return LogicModule(null, moduleName, membersGeneratedByParentClasses.toList())
    }
}

fun getModule(modules: List<LogicModule>, jClass: ModuleMember): List<LogicModule> {
    val callerByFullMatch = fullMatch(jClass, modules)
    if (callerByFullMatch.isNotEmpty()) {
        return callerByFullMatch
    }
    return startsWithMatch(jClass, modules)
}

private fun fullMatch(jClass: ModuleMember, modules: List<LogicModule>): List<LogicModule> {
    return modules.filter { logicModule ->
        logicModule.members.any { moduleMember -> jClass.getFullName() == moduleMember.getFullName() }
    }
}

fun startsWithMatch(jClass: ModuleMember, modules: List<LogicModule>): List<LogicModule> {
    var maxMatchSize = 0
    val matchModule: MutableList<LogicModule> = mutableListOf()
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
        throw NoModuleFoundException("$jClass No LogicModule matched!")
    }
    return matchModule.toList()
}

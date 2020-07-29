package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.domain.model.LogicModule
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
    lateinit var metricsService: MetricsService

    fun getLogicModules(): List<LogicModule> {
        return logicModuleRepository.getAll()
    }

    fun getLogicModule(name: String): LogicModule {
        return logicModuleRepository.get(name)
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
        metricsService.calculateCoupling()
    }

    fun createLogicModule(logicModule: LogicModule): String {
        logicModuleRepository.create(logicModule)
        return logicModule.id
    }

    fun createLogicModuleWithCompositeNodes(logicModule: LogicModuleWithCompositeNodes): String {
        logicModuleRepository.createWithCompositeNodes(logicModule)
        return logicModule.id!!
    }

    fun deleteLogicModule(id: String) {
        logicModuleRepository.delete(id)
        metricsService.calculateCoupling()
    }

    fun autoDefineLogicModule() {
        logicModuleRepository.deleteAll()
        val defaultModules = logicModuleRepository.getAllSubModule()
                .map { LogicModule.createWithOnlyLeafMembers(UUID.randomUUID().toString(), it.name, mutableListOf(it)) }
        logicModuleRepository.saveAll(defaultModules)
        metricsService.calculateCoupling()
    }

}

fun getModule(modules: List<LogicModule>, logicComponent: LogicComponent): List<LogicModule> {
    val callerByFullMatch = fullMatch(logicComponent, modules)
    if (callerByFullMatch.isNotEmpty()) {
        return callerByFullMatch
    }
    return startsWithMatch(logicComponent, modules)
}

private fun fullMatch(jClass: LogicComponent, modules: List<LogicModule>): List<LogicModule> {
    return modules.filter { logicModule ->
        logicModule.members.any { moduleMember -> jClass.getFullName() == moduleMember.getFullName() }
    }
}

private fun startsWithMatch(jClass: LogicComponent, modules: List<LogicModule>): List<LogicModule> {
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

    return matchModule.toList()
}

package com.thoughtworks.archguard.code.module.domain

import org.archguard.arch.LogicComponent
import com.thoughtworks.archguard.code.module.domain.model.LogicModule
import com.thoughtworks.archguard.metrics.domain.coupling.CouplingService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LogicModuleService(val logicModuleRepository: LogicModuleRepository, val couplingService: CouplingService) {
    fun getLogicModules(systemId: Long): List<LogicModule> {
        return logicModuleRepository.getAllBySystemId(systemId)
    }

    fun getLogicModule(systemId: Long, name: String): LogicModule {
        return logicModuleRepository.get(systemId, name)
    }

    fun hideAllLogicModules(systemId: Long) {
        val logicModules = getLogicModules(systemId)
        logicModules.forEach { it.hide() }
        logicModuleRepository.updateAll(logicModules)
    }

    fun showAllLogicModules(systemId: Long) {
        val logicModules = getLogicModules(systemId)
        logicModules.forEach { it.show() }
        logicModuleRepository.updateAll(logicModules)
    }

    fun reverseAllLogicModulesStatus(systemId: Long) {
        val logicModules = getLogicModules(systemId)
        logicModules.forEach { it.reverse() }
        logicModuleRepository.updateAll(logicModules)
    }

    fun updateLogicModule(systemId: Long, id: String, logicModule: LogicModule) {
        logicModuleRepository.update(id, logicModule)
        couplingService.persistAllClassCouplingResults(systemId)
    }

    fun createLogicModule(systemId: Long, logicModule: LogicModule): String {
        logicModuleRepository.create(systemId, logicModule)
        return logicModule.id
    }

    fun createLogicModuleWithCompositeNodes(systemId: Long, logicModule: LogicModuleWithCompositeNodes): String {
        logicModuleRepository.createWithCompositeNodes(systemId, logicModule)
        return logicModule.id!!
    }

    fun deleteLogicModule(systemId: Long, id: String) {
        logicModuleRepository.delete(id)
        couplingService.persistAllClassCouplingResults(systemId)
    }

    fun autoDefineLogicModule(systemId: Long) {
        logicModuleRepository.deleteBySystemId(systemId)
        val defaultModules = logicModuleRepository.getAllSubModule(systemId)
            .map { LogicModule.createWithOnlyLeafMembers(UUID.randomUUID().toString(), it.name, mutableListOf(it)) }
        logicModuleRepository.saveAll(systemId, defaultModules)
        couplingService.persistAllClassCouplingResults(systemId)
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
            .maxByOrNull { it.getFullName().length }
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

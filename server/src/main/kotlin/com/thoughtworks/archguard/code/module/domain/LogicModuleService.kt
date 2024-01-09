package com.thoughtworks.archguard.code.module.domain

import org.archguard.arch.LogicModule
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


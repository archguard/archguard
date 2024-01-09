package com.thoughtworks.archguard.code.module.infrastructure

import com.thoughtworks.archguard.code.module.domain.LogicModuleRepository
import org.archguard.arch.LeafManger
import org.archguard.arch.LogicModule
import org.archguard.arch.LogicModuleStatus

class LogicModuleDTO(val id: String, val name: String, val members: String?, private val lgMembers: String?, private val status: LogicModuleStatus) {
    fun toLogicModule(systemId: Long, logicModuleRepository: LogicModuleRepository): LogicModule {
        val leafMembers = members?.split(',')?.sorted()?.map { m -> LeafManger.createLeaf(m) } ?: emptyList()
        val lgMembers = lgMembers?.split(',')?.sorted()?.map { m -> logicModuleRepository.get(systemId, m) }
            ?: emptyList()
        val logicModule = LogicModule.create(id, name, leafMembers, lgMembers)
        logicModule.status = status
        return logicModule
    }
}
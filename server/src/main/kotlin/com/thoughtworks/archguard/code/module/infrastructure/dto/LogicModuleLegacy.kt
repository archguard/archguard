package com.thoughtworks.archguard.code.module.infrastructure.dto

import com.thoughtworks.archguard.code.module.domain.model.LogicComponent
import com.thoughtworks.archguard.code.module.domain.model.LogicModule
import com.thoughtworks.archguard.code.module.domain.model.LogicModuleStatus
import java.util.UUID

@Deprecated(message = "we are going to replace with LogicModule")
class LogicModuleLegacy(var id: String?, val name: String, val members: List<String>, var status: LogicModuleStatus = LogicModuleStatus.NORMAL) {
    fun toLogicModule(): LogicModule {
        if (id == null) {
            id = UUID.randomUUID().toString()
        }
        val logicModule = LogicModule.createWithOnlyLeafMembers(id!!, name, members.map { LogicComponent.createLeaf(it) })
        logicModule.status = status
        return logicModule
    }

    companion object {
        fun fromLogicModule(logicModule: LogicModule): LogicModuleLegacy {
            return LogicModuleLegacy(logicModule.id, logicModule.name, logicModule.members.map { it.getFullName() }, logicModule.status)
        }
    }
}

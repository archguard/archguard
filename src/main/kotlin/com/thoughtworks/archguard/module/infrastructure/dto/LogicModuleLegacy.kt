package com.thoughtworks.archguard.module.infrastructure.dto

import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.LogicModuleStatus
import java.util.*

@Deprecated(message = "we are going to replace with LogicModule")
class LogicModuleLegacy(var id: String?, val name: String, val members: List<String>, var status: LogicModuleStatus = LogicModuleStatus.NORMAL) {
    fun toLogicModule(): LogicModule {
        if (id == null) {
            id = UUID.randomUUID().toString()
        }
        // TODO: 前端暂时还不支持logicModule作为member存储
        val logicModule = LogicModule.create(id!!, name, members.map { LogicComponent.createLeaf(it) }, emptyList())
        logicModule.status = status
        return logicModule
    }

    companion object {
        fun fromLogicModule(logicModule: LogicModule): LogicModuleLegacy {
            return LogicModuleLegacy(logicModule.id, logicModule.name, logicModule.members.map { it.getFullName() }, logicModule.status)
        }
    }
}

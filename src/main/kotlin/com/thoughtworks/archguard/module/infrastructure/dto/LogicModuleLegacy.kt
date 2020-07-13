package com.thoughtworks.archguard.module.infrastructure.dto

import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.LogicModuleStatus
import com.thoughtworks.archguard.module.domain.model.ModuleMember
import java.util.*

@Deprecated(message = "we are going to replace with LogicModule")
class LogicModuleLegacy(var id: String?, val name: String, val members: List<String>, var status: LogicModuleStatus = LogicModuleStatus.NORMAL) {
    fun toLogicModule(): LogicModule {
        if (id == null) {
            id = UUID.randomUUID().toString()
        }
        return LogicModule(id!!, name, members.map { ModuleMember.create(it) }, status)
    }

    companion object {
        fun fromLogicModule(logicModule: LogicModule): LogicModuleLegacy {
            return LogicModuleLegacy(logicModule.id, logicModule.name, logicModule.members.map { it.getFullName() }, logicModule.status)
        }
    }
}

// For Database
class LogicModuleDTO(val id: String, val name: String, val members: String, val status: LogicModuleStatus) {
    fun toLogicModule(): LogicModule {
        return LogicModule(id, name, members.split(',').sorted()
                .map { m -> ModuleMember.create(m) }, status)
    }
}
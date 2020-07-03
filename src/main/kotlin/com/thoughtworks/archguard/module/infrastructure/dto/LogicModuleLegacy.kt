package com.thoughtworks.archguard.module.infrastructure.dto

import com.thoughtworks.archguard.module.domain.LogicModule
import com.thoughtworks.archguard.module.domain.LogicModuleStatus
import com.thoughtworks.archguard.module.domain.ModuleMember

@Deprecated(message = "we are going to replace with LogicModule")
class LogicModuleLegacy(var id: String?, val name: String, val members: List<String>, var status: LogicModuleStatus = LogicModuleStatus.NORMAL) {
    fun toLogicModule(): LogicModule {
        return LogicModule(id, name, members.map { ModuleMember.createModuleMember(it) }, status)
    }
}

fun fromLogicModule(logicModule: LogicModule): LogicModuleLegacy {
    return LogicModuleLegacy(logicModule.id, logicModule.name, logicModule.members.map { it.getFullName() }, logicModule.status)
}

// For Database
data class LogicModuleDTO(val id: String, val name: String, val members: String, val status: LogicModuleStatus)
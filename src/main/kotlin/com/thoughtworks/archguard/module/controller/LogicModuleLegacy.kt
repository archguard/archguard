package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.module.domain.LogicModule
import com.thoughtworks.archguard.module.domain.LogicModuleStatus
import com.thoughtworks.archguard.module.domain.createModuleMember

@Deprecated(message = "we are going to replace with LogicModule")
class LogicModuleLegacy(var id: String?, val name: String, val members: List<String>, var status: LogicModuleStatus = LogicModuleStatus.NORMAL) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LogicModuleLegacy

        if (id != other.id) return false
        if (name != other.name) return false
        if (members != other.members) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + members.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }

    fun toLogicModule(): LogicModule {
        return LogicModule(id, name, members.map { createModuleMember(it) }, status)
    }
}

fun fromLogicModule(logicModule: LogicModule): LogicModuleLegacy {
    return LogicModuleLegacy(logicModule.id, logicModule.name, logicModule.members.map { it.getFullName() }, logicModule.status)
}
package com.thoughtworks.archguard.code.module.domain

import com.thoughtworks.archguard.code.module.domain.model.LogicModuleStatus
import java.util.*

class LogicModuleWithCompositeNodes(var id: String?, val name: String, val lgMembers: List<String>, var status: LogicModuleStatus = LogicModuleStatus.NORMAL) {
    fun toLogicModule(): LogicModuleWithCompositeNodes {
        if (id == null) {
            id = UUID.randomUUID().toString()
        }
        val logicModule = LogicModuleWithCompositeNodes(id, name, lgMembers)
        logicModule.status = status
        return logicModule
    }
}
package com.thoughtworks.archguard.module.domain

data class LogicModule(var id: String?, val name: String, val members: List<String>, var status: LogicModuleStatus = LogicModuleStatus.NORMAL)

enum class LogicModuleStatus {
    NORMAL, HIDE
}
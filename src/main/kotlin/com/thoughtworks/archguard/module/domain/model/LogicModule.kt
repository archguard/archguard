package com.thoughtworks.archguard.module.domain.model

class LogicModule(var id: String?, val name: String, val members: List<ModuleMember>, var status: LogicModuleStatus = LogicModuleStatus.NORMAL) {
    fun hide() {
        this.status = LogicModuleStatus.HIDE
    }

    fun show() {
        this.status = LogicModuleStatus.NORMAL
    }

    fun reverse() {
        if (isNormalStatus()) {
            hide()
            return
        }
        if (isHideStatus()) {
            show()
            return
        }
        throw RuntimeException("Illegal logic module status!")
    }

    private fun isHideStatus() = this.status == LogicModuleStatus.HIDE

    private fun isNormalStatus() = this.status == LogicModuleStatus.NORMAL

    override fun toString(): String {
        return "LogicModule(id=$id, name='$name', members=$members, status=$status)"
    }
}

enum class LogicModuleStatus {
    NORMAL, HIDE
}
package com.thoughtworks.archguard.module.domain

class NewLogicModule(var id: String?, val name: String, val members: List<ModuleMember>, var status: LogicModuleStatus = LogicModuleStatus.NORMAL) {
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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewLogicModule

        if (name != other.name) return false
        if (members != other.members) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + members.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }


}
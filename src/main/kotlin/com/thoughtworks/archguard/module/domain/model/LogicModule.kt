package com.thoughtworks.archguard.module.domain.model

/**
 * LogicModule is an Entity, so it must have an id.
 */
class LogicModule(val id: String, val name: String, val members: List<ModuleMember>) {
    var status = LogicModuleStatus.NORMAL
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LogicModule

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

enum class LogicModuleStatus {
    NORMAL, HIDE
}
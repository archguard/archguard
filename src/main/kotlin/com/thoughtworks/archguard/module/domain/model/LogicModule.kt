package com.thoughtworks.archguard.module.domain.model

/**
 * LogicModule is an Entity, so it must have an id.
 */
class LogicModule(val id: String, val name: String, val members: List<LogicComponent>) : LogicComponent() {
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

    override fun add(logicComponent: LogicComponent) {
        TODO("Not yet implemented")
    }

    override fun remove(logicComponent: LogicComponent) {
        TODO("Not yet implemented")
    }

    override fun getSubLogicComponent(): List<LogicComponent> {
        TODO("Not yet implemented")
    }

    override fun getFullName(): String {
        return name
    }

    override fun getType(): ModuleMemberType {
        return ModuleMemberType.LOGIC_MODULE
    }

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
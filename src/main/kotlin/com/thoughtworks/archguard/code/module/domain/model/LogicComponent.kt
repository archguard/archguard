package com.thoughtworks.archguard.code.module.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore

abstract class LogicComponent {
    open fun add(logicComponent: LogicComponent) {
        throw RuntimeException("Not Impl in abstract class or leaf node.")
    }

    open fun remove(logicComponent: LogicComponent) {
        throw RuntimeException("Not Impl in abstract class or leaf node.")
    }

    @JsonIgnore
    open fun getSubLogicComponent(): List<LogicComponent> {
        throw RuntimeException("Not Impl in abstract class or leaf node.")
    }

    abstract fun containsOrEquals(logicComponent: LogicComponent): Boolean
    abstract fun getFullName(): String
    abstract fun getType(): ModuleMemberType

    abstract override fun toString(): String

    companion object {
        fun createLeaf(name: String): LogicComponent {
            if (name.split(".").size > 1) {
                return JClassVO.create(name)
            }
            return SubModule(name)
        }
    }
}

enum class ModuleMemberType {
    SUBMODULE, CLASS, LOGIC_MODULE
}

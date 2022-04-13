package com.thoughtworks.archguard.code.module.domain.model

import org.slf4j.LoggerFactory

/**
 * SubModule is a Value Object, use for LogicModule aggregation
 */
class SubModule(val name: String) : LogicComponent() {
    private val log = LoggerFactory.getLogger(SubModule::class.java)

    override fun containsOrEquals(logicComponent: LogicComponent): Boolean {
        return logicComponent.getType() == ModuleMemberType.SUBMODULE && logicComponent.getFullName() == this.getFullName()
    }

    override fun getFullName(): String {
        return name
    }

    override fun getType(): ModuleMemberType {
        return ModuleMemberType.SUBMODULE
    }

    override fun toString(): String {
        return "SubModule(name='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SubModule

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}
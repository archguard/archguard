package org.archguard.arch

import org.slf4j.LoggerFactory

/**
 * SubModule is a Value Object, use for LogicModule aggregation
 */
data class SubModule(val name: String) : LogicComponent() {
    private val log = LoggerFactory.getLogger(SubModule::class.java)

    override fun containsOrEquals(logicComponent: LogicComponent): Boolean {
        return logicComponent.getType() == LogicModuleMemberType.SUBMODULE && logicComponent.getFullName() == this.getFullName()
    }

    override fun getFullName(): String {
        return name
    }

    override fun getType(): LogicModuleMemberType {
        return LogicModuleMemberType.SUBMODULE
    }
}

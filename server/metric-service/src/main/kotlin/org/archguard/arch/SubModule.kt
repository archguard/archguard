package org.archguard.arch

import org.slf4j.LoggerFactory

/**
 * SubModule is a Value Object that represents a submodule.
 * It is used for aggregation in the [LogicModule].
 *
 * @property name The name of the submodule.
 */
data class SubModule(val name: String) : LogicComponent() {
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

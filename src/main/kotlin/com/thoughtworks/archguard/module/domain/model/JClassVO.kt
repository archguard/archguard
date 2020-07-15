package com.thoughtworks.archguard.module.domain.model

/**
 * JClassVO is a Value Object, use for LogicModule aggregation
 */
class JClassVO(val name: String, val module: String) : LogicComponent() {
    override fun containsOrEquals(logicComponent: LogicComponent): Boolean {
        return logicComponent.getType() == ModuleMemberType.CLASS && logicComponent.getFullName() == this.getFullName()
    }

    companion object {
        fun create(fullName: String): JClassVO {
            val split = fullName.split(".")
            return JClassVO(split.subList(1, split.size).joinToString(".").trim(), split[0].trim())
        }
    }

    override fun getFullName(): String {
        return "$module.$name"
    }

    override fun getType(): ModuleMemberType {
        return ModuleMemberType.CLASS
    }

    override fun toString(): String {
        return "JClassVO(name='$name', module='$module')"
    }

}
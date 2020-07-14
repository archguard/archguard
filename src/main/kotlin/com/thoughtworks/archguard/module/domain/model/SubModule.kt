package com.thoughtworks.archguard.module.domain.model

class SubModule(val name: String) : LogicComponent() {
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
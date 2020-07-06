package com.thoughtworks.archguard.module.domain.model

class SubModule(val name: String) : ModuleMember {
    override fun getFullName(): String {
        return name
    }

    override fun getType(): ModuleMemberType {
        return ModuleMemberType.SUBMODULE
    }

    override fun toString(): String {
        return "SubModule(name='$name')"
    }


}
package com.thoughtworks.archguard.module.domain.model

interface ModuleMember {
    fun getFullName(): String
    fun getType(): ModuleMemberType

    companion object {
        fun create(name: String): ModuleMember {
            if (name.split(".").size > 1) {
                return JClass.create(name)
            }
            return SubModule(name)
        }
    }
}

enum class ModuleMemberType {
    SUBMODULE, CLASS
}
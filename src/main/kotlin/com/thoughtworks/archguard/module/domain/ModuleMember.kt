package com.thoughtworks.archguard.module.domain

interface ModuleMember {
    fun getFullName(): String
    fun getType(): ModuleMemberType

    companion object {
        fun createModuleMember(name: String): ModuleMember {
            if (name.split(".").size > 1) {
                return JClass.createJClassFromFullName(name)
            }
            return SubModule(name)
        }
    }
}

enum class ModuleMemberType {
    SUBMODULE, CLASS
}
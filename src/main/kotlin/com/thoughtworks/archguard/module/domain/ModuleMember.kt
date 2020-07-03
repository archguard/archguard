package com.thoughtworks.archguard.module.domain

interface ModuleMember {
    fun getFullName(): String
    fun getType(): ModuleMemberType
}

fun createModuleMember(name: String): ModuleMember {
    if (name.split(".").size > 1) {
        return createJClassFromFullName(name)
    }
    return SubModule(name)
}

enum class ModuleMemberType {
    SUBMODULE, CLASS
}
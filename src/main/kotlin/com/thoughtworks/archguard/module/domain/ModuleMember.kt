package com.thoughtworks.archguard.module.domain

interface ModuleMember {
    fun getFullName(): String
    fun getType(): ModuleMemberType
}

enum class ModuleMemberType {
    SUBMODULE, CLASS
}
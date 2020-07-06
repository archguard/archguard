package com.thoughtworks.archguard.module.domain.model

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

class JClass(val name: String, val module: String) : ModuleMember {
    var id: String? = null
    var classType: ClazzType = ClazzType.NOT_DEFINED

    @JdbiConstructor
    constructor(id: String, name: String, module: String) : this(name, module) {
        this.id = id
    }

    companion object {
        fun create(fullName: String): JClass {
            val split = fullName.split(".")
            return JClass(split.subList(1, split.size).joinToString(".").trim(), split[0].trim())
        }
    }

    override fun getFullName(): String {
        return "$module.$name"
    }

    override fun getType(): ModuleMemberType {
        return ModuleMemberType.CLASS
    }

    override fun toString(): String {
        return "JClass(name='$name', module='$module', id='$id')"
    }
}

// 暂时只有接口和类
enum class ClazzType {
    INTERFACE, CLASS, NOT_DEFINED
}

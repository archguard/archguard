package com.thoughtworks.archguard.module.domain.model

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

class JClass(val name: String, val module: String) : ModuleMember {
    lateinit var id: String

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JClass

        if (name != other.name) return false
        if (module != other.module) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + module.hashCode()
        return result
    }

    override fun toString(): String {
        return "JClass(name='$name', module='$module', id='$id')"
    }
}
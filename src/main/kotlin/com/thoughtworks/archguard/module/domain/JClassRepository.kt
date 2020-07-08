package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.ModuleMember

interface JClassRepository {
    fun getJClassBy(name: String, module: String): JClass?

    fun getJClassByName(name: String): List<JClass>

    fun getJClassById(id: String): JClass?

    fun getAll(): List<JClass>

    fun getAll(fullNames: List<FullName>): List<JClass>

    fun getJClassesHasModules(): List<JClass>

    fun findDependencees(id: String?): List<JClass>

    fun findDependencers(id: String?): List<JClass>

    fun getAllClassDependency(members: List<ModuleMember>): List<Dependency<JClass>>

}

class FullName(val name: String, val module: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FullName

        if (name != other.name) return false
        if (module != other.module) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + module.hashCode()
        return result
    }
}
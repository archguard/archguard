package com.thoughtworks.archguard.clazz.domain

import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicComponent

interface JClassRepository {
    fun getJClassBy(name: String, module: String): JClass?

    fun getJClassByName(name: String): List<JClass>

    fun getJClassById(id: String): JClass?

    fun getAll(): List<JClass>

    fun getAll(fullNames: List<FullName>): List<JClass>

    fun getJClassesHasModules(): List<JClass>

    fun findDependencees(id: String): List<JClass>

    fun findDependencers(id: String): List<JClass>

    fun getAllClassDependency(members: List<LogicComponent>): List<Dependency<JClass>>

    fun findClassParents(module: String?, name: String?): List<JClass>

    fun findClassImplements(name: String?, module: String?): List<JClass>

    fun findCallees(name: String?, module: String?): List<ClassRelation>

    fun findCallers(name: String?, module: String?): List<ClassRelation>
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

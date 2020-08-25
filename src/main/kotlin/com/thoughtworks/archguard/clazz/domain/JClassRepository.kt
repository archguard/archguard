package com.thoughtworks.archguard.clazz.domain

interface JClassRepository {
    fun getJClassBy(name: String, module: String): JClass?

    fun getJClassById(id: String): JClass?

    fun getAllByProjectId(projectId: Long): List<JClass>

    fun getAll(fullNames: List<FullName>): List<JClass>

    fun getJClassesHasModules(projectId: Long): List<JClass>

    fun findDependencees(id: String): List<JClass>

    fun findDependencers(id: String): List<JClass>

    fun findClassParents(module: String?, name: String?): List<JClass>

    fun findClassImplements(name: String?, module: String?): List<JClass>

    fun findCallees(name: String?, module: String?): List<ClassRelation>

    fun findCallers(name: String?, module: String?): List<ClassRelation>

    fun findFields(id: String): List<JField>
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

package com.thoughtworks.archguard.clazz.domain

interface JClassRepository {
    fun getJClassBy(systemId: Long, name: String, module: String?): JClass?

    fun getJClassById(id: String): JClass?

    fun getAllBySystemId(systemId: Long): List<JClass>

    fun getJClassesHasModules(systemId: Long): List<JClass>

    fun findDependencees(id: String): List<JClass>

    fun findDependencers(id: String): List<JClass>

    fun findClassParents(systemId: Long, module: String, name: String): List<JClass>

    fun findClassImplements(systemId: Long, name: String, module: String): List<JClass>

    fun findCallees(systemId: Long, name: String, module: String): List<ClassRelation>

    fun findCallers(systemId: Long, name: String, module: String): List<ClassRelation>

    fun findFields(id: String): List<JField>
}

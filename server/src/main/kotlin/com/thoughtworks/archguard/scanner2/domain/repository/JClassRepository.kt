package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.model.Dependency
import org.archguard.model.code.JClass
import org.archguard.model.code.JField

interface JClassRepository {

    fun getJClassesNotThirdPartyAndNotTest(systemId: Long): List<JClass>

    fun findClassParents(systemId: Long, module: String?, name: String?): List<JClass>

    fun findClassImplements(systemId: Long, name: String?, module: String?): List<JClass>

    fun findFields(id: String): List<JField>

    fun getDistinctClassDependenciesAndNotThirdParty(systemId: Long): List<Dependency<String>>

    fun getAllClassDependenciesAndNotThirdParty(systemId: Long): List<Dependency<String>>

    fun findClassBy(systemId: Long, name: String, module: String?): JClass?
}

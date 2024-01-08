package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.model.Dependency
import org.archguard.model.code.JField
import org.archguard.model.code.JMethod

interface JMethodRepository {
    fun findMethodsByModuleAndClass(systemId: Long, module: String?, name: String): List<JMethod>
    fun findMethodCallees(id: String): List<JMethod>
    fun findMethodFields(id: String): List<JField>
    fun getAllMethodDependenciesAndNotThirdParty(systemId: Long): List<Dependency<String>>
    fun getDistinctMethodDependenciesAndNotThirdParty(systemId: Long): List<Dependency<String>>
    fun getMethodsNotThirdParty(systemId: Long): List<JMethod>
    fun getMethodsNotThirdPartyAndNotTest(systemId: Long): List<JMethod>
}

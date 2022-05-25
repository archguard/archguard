package com.thoughtworks.archguard.scanner2.domain.repository

import com.thoughtworks.archguard.scanner2.domain.model.Dependency
import com.thoughtworks.archguard.scanner2.domain.model.JField
import com.thoughtworks.archguard.scanner2.domain.model.JMethod

interface JMethodRepository {
    fun findMethodsByModuleAndClass(systemId: Long, module: String?, name: String): List<JMethod>
    fun findMethodCallees(id: String): List<JMethod>
    fun findMethodFields(id: String): List<JField>
    fun getAllMethodDependenciesAndNotThirdParty(systemId: Long): List<Dependency<String>>
    fun getDistinctMethodDependenciesAndNotThirdParty(systemId: Long): List<Dependency<String>>
    fun getMethodsNotThirdParty(systemId: Long): List<JMethod>
    fun getMethodsNotThirdPartyAndNotTest(systemId: Long): List<JMethod>
}

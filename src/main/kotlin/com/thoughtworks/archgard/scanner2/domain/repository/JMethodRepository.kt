package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.JField
import com.thoughtworks.archgard.scanner2.domain.model.JMethod
import com.thoughtworks.archgard.scanner2.domain.service.Dependency

interface JMethodRepository {
    fun findMethodsByModuleAndClass(systemId: Long, module: String, name: String): List<JMethod>
    fun findMethodCallees(id: String): List<JMethod>
    fun findMethodFields(id: String): List<JField>
    fun getAllMethodDependencies(systemId: Long): List<Dependency<String>>
    fun getMethodsHasModules(systemId: Long): List<JMethod>
}


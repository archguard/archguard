package com.thoughtworks.archgard.scanner2.domain

import com.thoughtworks.archgard.scanner2.domain.model.JField
import com.thoughtworks.archgard.scanner2.domain.model.JMethod

interface JMethodRepository {
    fun findMethodsByModuleAndClass(systemId: Long, module: String, name: String): List<JMethod>
    fun findMethodCallees(id: String): List<JMethod>
    fun findMethodFields(id: String): List<JField>
}

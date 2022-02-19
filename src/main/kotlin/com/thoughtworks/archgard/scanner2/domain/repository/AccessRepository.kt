package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.ClassAccess
import com.thoughtworks.archgard.scanner2.domain.model.MethodAccess

interface AccessRepository {
    fun saveOrUpdateAllClass(systemId: Long, classAccesses: List<ClassAccess>)
    fun saveOrUpdateAllMethod(systemId: Long, methodAccesses: List<MethodAccess>)
}

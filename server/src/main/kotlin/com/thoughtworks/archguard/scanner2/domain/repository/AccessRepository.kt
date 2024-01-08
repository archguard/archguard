package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.model.code.ClassAccess
import org.archguard.model.code.MethodAccess

interface AccessRepository {
    fun saveOrUpdateAllClass(systemId: Long, classAccesses: List<ClassAccess>)
    fun saveOrUpdateAllMethod(systemId: Long, methodAccesses: List<MethodAccess>)
}

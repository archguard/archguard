package com.thoughtworks.archguard.scanner2.domain.service

import org.archguard.model.code.ClassAccess
import org.archguard.model.code.MethodAccess
import com.thoughtworks.archguard.scanner2.domain.repository.AccessRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
import org.springframework.stereotype.Service

@Service
class AccessService(
    val accessRepository: AccessRepository,
    val jClassRepository: JClassRepository,
    val jMethodRepository: JMethodRepository
) {
    fun persist(systemId: Long) {
        val classes = jClassRepository.getJClassesNotThirdPartyAndNotTest(systemId)
        accessRepository.saveOrUpdateAllClass(systemId, ClassAccess.from(classes))

        val methods = jMethodRepository.getMethodsNotThirdParty(systemId)
        accessRepository.saveOrUpdateAllMethod(systemId, MethodAccess.from(methods))
    }
}

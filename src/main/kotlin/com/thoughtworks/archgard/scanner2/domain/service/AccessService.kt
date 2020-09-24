package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.model.ClassAccess
import com.thoughtworks.archgard.scanner2.domain.model.MethodAccess
import com.thoughtworks.archgard.scanner2.domain.repository.AccessRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import org.springframework.stereotype.Service

@Service
class AccessService(val accessRepository: AccessRepository,
                    val jClassRepository: JClassRepository, val jMethodRepository: JMethodRepository) {
    fun persist(systemId: Long) {
        val classes = jClassRepository.getJClassesNotThirdParty(systemId)
        accessRepository.saveOrUpdateAllClass(systemId, ClassAccess.from(classes))

        val methods = jMethodRepository.getMethodsNotThirdParty(systemId)
        accessRepository.saveOrUpdateAllMethod(systemId, MethodAccess.from(methods))
    }

}

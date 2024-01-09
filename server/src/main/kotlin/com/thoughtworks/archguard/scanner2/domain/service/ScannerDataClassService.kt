package com.thoughtworks.archguard.scanner2.domain.service

import org.archguard.model.code.JClass
import com.thoughtworks.archguard.scanner2.domain.repository.DataClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
import org.archguard.operator.DataClassChecker
import org.springframework.stereotype.Service

@Service
class ScannerDataClassService(val jClassRepository: JClassRepository, val jMethodRepository: JMethodRepository, val dataClassRepository: DataClassRepository) {
    fun findAllDataClasses(systemId: Long): List<JClass> {
        val jClasses = jClassRepository.getJClassesNotThirdPartyAndNotTest(systemId)
        val dataClasses = mutableListOf<JClass>()
        jClasses.forEach {
            it.fields = jClassRepository.findFields(it.id)
            it.methods = jMethodRepository.findMethodsByModuleAndClass(systemId, it.module, it.name).filterNot {
                it.name in listOf("toString", "equals", "hashCode", "<init>", "<clinit>", "main")
            }
            it.methods.forEach { method ->
                method.fields = jMethodRepository.findMethodFields(method.id)
            }
            if (DataClassChecker.check(it)) {
                dataClasses.add(it)
            }
        }
        return dataClasses
    }
}


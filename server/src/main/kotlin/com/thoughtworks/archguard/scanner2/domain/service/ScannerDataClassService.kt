package com.thoughtworks.archguard.scanner2.domain.service

import com.thoughtworks.archguard.scanner2.domain.model.JClass
import com.thoughtworks.archguard.scanner2.domain.repository.DataClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
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
            if (checkIsDataClass(it)) {
                dataClasses.add(it)
            }
        }
        return dataClasses
    }
}

fun checkIsDataClass(jClass: JClass): Boolean {
    if (jClass.fields.size * 2 != jClass.methods.size) {
        return false
    }
    jClass.fields.forEach {
        val methodNames = jClass.methods.map { it.name }.toMutableList()
        if (it.type in listOf("java.lang.Boolean", "boolean")) {
            if (!methodNames.contains("is" + capitalize(it.name)) || !methodNames.contains("set" + capitalize(it.name))) {
                return false
            }
        } else if (!methodNames.contains("get" + capitalize(it.name)) || !methodNames.contains("set" + capitalize(it.name))) {
            return false
        }
    }
    return true
}

fun capitalize(line: String): String {
    if (line.isEmpty()) {
        return line
    }
    return Character.toUpperCase(line[0]).toString() + line.substring(1)
}

package com.thoughtworks.archgard.scanner2.domain

import com.thoughtworks.archgard.scanner2.domain.model.ClassNoc
import com.thoughtworks.archgard.scanner2.domain.model.JClass
import org.springframework.stereotype.Service

@Service
class NocService(val jClassRepository: JClassRepository) {

    fun calculateAllNoc(systemId: Long): List<ClassNoc> {
        val jClasses = jClassRepository.getJClassesHasModules(systemId)
        val classNocList = mutableListOf<ClassNoc>()
        jClasses.forEach { classNocList.add(ClassNoc(it.toVO(), getNoc(systemId, it))) }
        return classNocList
    }

    fun getNoc(systemId: Long, jClass: JClass): Int {
        return getNodeCount(systemId, jClass) - 1
    }

    fun getNodeCount(systemId: Long, jClass: JClass): Int {
        val implements = jClassRepository.findClassImplements(systemId, jClass.name, jClass.module)
        if (implements.isEmpty()) {
            return 1
        }
        return implements.map { getNodeCount(systemId, it) }.sum() + 1
    }

}

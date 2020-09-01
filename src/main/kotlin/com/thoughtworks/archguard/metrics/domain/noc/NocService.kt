package com.thoughtworks.archguard.metrics.domain.noc

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.metrics.domain.ClassNoc
import org.springframework.stereotype.Service

@Service
class NocService(val jClassRepository: JClassRepository) {

    fun calculateAllNoc(systemId: Long): List<ClassNoc> {
        val jClasses = jClassRepository.getJClassesHasModules(systemId)
        val classNocList = mutableListOf<ClassNoc>()
        jClasses.forEach { classNocList.add(ClassNoc(it.toVO(), getNoc(systemId, it))) }
        return classNocList
    }

    fun getNodeCount(systemId: Long, jClass: JClass): Int {
        val implements = jClassRepository.findClassImplements(systemId, jClass.name, jClass.module)
        if (implements.isEmpty()) {
            return 1
        }
        return implements.map { getNodeCount(systemId, it) }.sum() + 1
    }

    fun getNoc(systemId: Long, jClass: JClass): Int {
        return getNodeCount(systemId, jClass) - 1
    }

}

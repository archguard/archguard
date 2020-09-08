package com.thoughtworks.archgard.scanner2.domain

import com.thoughtworks.archgard.scanner2.domain.model.ClassDit
import com.thoughtworks.archgard.scanner2.domain.model.JClass
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DitService(val jClassRepository: JClassRepository) {

    private val log = LoggerFactory.getLogger(DitService::class.java)

    fun calculateAllDit(systemId: Long): List<ClassDit> {
        val jClasses = jClassRepository.getJClassesHasModules(systemId)
        val classDitList = mutableListOf<ClassDit>()
        jClasses.forEach { classDitList.add(ClassDit(it.toVO(), getDepthOfInheritance(systemId, it))) }
        return classDitList
    }

    fun getDepthOfInheritance(systemId: Long, target: JClass): Int {
        val parents = jClassRepository.findClassParents(systemId, target.module, target.name)
        return findInheritanceDepth(systemId, parents)
    }

    private fun findInheritanceDepth(systemId: Long, superClasses: List<JClass>): Int {
        if (superClasses.isEmpty()) {
            return 0
        }
        val deeperSuperClasses = mutableListOf<JClass>()
        for (superClass in superClasses) {
            deeperSuperClasses.addAll(jClassRepository.findClassParents(systemId, superClass.module, superClass.name))
        }
        return try {
            findInheritanceDepth(systemId, deeperSuperClasses) + 1
        } catch (ex: StackOverflowError) {
            log.error("Inheritance depth analysis step skipped due to memory overflow.")
            0
        }
    }
}

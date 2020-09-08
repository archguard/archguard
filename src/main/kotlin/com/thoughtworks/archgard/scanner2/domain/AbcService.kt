package com.thoughtworks.archgard.scanner2.domain

import com.thoughtworks.archgard.scanner2.domain.model.ClassAbc
import com.thoughtworks.archgard.scanner2.domain.model.JClass
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Association Between Class (ABC): The Association Between Classes metric for a particular class or structure
 * is the number of members of others types it directly uses in the body of its methods.
 */
@Service
class AbcService(val jMethodRepository: JMethodRepository,
                 val jClassRepository: JClassRepository) {

    fun calculateAllAbc(systemId: Long): List<ClassAbc> {

        val jClasses = jClassRepository.getJClassesHasModules(systemId)
        jClasses.forEach { it.methods = jMethodRepository.findMethodsByModuleAndClass(systemId, it.module, it.name) }

        val classAbcList = mutableListOf<ClassAbc>()
        jClasses.forEach { classAbcList.add(ClassAbc(it.toVO(), this.calculateAbc(it))) }

        return classAbcList
    }

    fun calculateAbc(jClass: JClass): Int {
        val allMethod = jClass.methods.map { jMethodRepository.findMethodCallees(it.id) }.flatten().map { it.toVO() }
        return allMethod.asSequence().map { it.clazz }
                .filter { it.module != "null" }
                .filter { it.module != jClass.module }
                .toSet().count()
    }
}
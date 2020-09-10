package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.model.JClass
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Association Between Class (ABC): The Association Between Classes metric for a particular class or structure
 * is the number of members of others types it directly uses in the body of its methods.
 */
@Service
class AbcService(val jMethodRepository: JMethodRepository) {

    private val log = LoggerFactory.getLogger(AbcService::class.java)

    fun calculate(systemId: Long, jClasses: List<JClass>): Map<String, Int> {
        jClasses.forEach { it.methods = jMethodRepository.findMethodsByModuleAndClass(systemId, it.module, it.name) }

        val abcMap: MutableMap<String, Int> = mutableMapOf()
        jClasses.forEach { abcMap[it.id] = this.calculateAbc(it) }
        log.info("Finish calculate all ABC, count: {}", abcMap.keys.size)

        return abcMap
    }

    fun calculateAbc(jClass: JClass): Int {
        val allMethodCallees = jClass.methods.map { jMethodRepository.findMethodCallees(it.id) }.flatten().map { it.toVO() }
        return allMethodCallees.asSequence().map { it.clazz }
                .filter { it.module != "null" }
                .filter { it.module != jClass.module }
                .toSet().count()
    }
}
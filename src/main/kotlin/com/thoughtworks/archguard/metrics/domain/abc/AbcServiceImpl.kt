package com.thoughtworks.archguard.metrics.domain.abc

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.springframework.stereotype.Service

/**
 * Association Between Class (ABC): The Association Between Classes metric for a particular class or structure
 * is the number of members of others types it directly uses in the body of its methods.
 */
@Service
class AbcServiceImpl(val jMethodRepository: JMethodRepository) : AbcService {
    override fun calculateAbc(jClass: JClass): Int {
        val allMethod = jClass.methods.map { jMethodRepository.findMethodCallees(it.id) }.flatten().map { it.toVO() }
        return allMethod.asSequence().map { it.clazz }.filter { it.module != "null" }.filter { it.module != jClass.module }.toSet().count()
    }
}
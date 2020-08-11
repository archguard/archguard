package com.thoughtworks.archguard.metrics.domain.abc

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.method.domain.JMethodRepository
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import org.springframework.stereotype.Service

@Service
class AbcServiceImpl(val jMethodRepository: JMethodRepository) : AbcService {
    override fun calculateAbc(jClass: JClass): Int {
        val map = jClass.methods.map { jMethodRepository.findMethodCallees(it.id) }.flatten().map { JMethodVO.fromJMethod(it) }
        return map.toSet().count()
    }
}
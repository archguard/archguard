package com.thoughtworks.archguard.metrics.domain.noc

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import org.springframework.stereotype.Service

@Service
class NocServiceImpl(val jClassRepository: JClassRepository) : NocService {
    fun getNodeCount(jClass: JClass): Int {
        val implements = jClassRepository.findClassImplements(jClass.name, jClass.module)
        if (implements.isEmpty()) {
            return 1
        }
        return implements.map { getNodeCount(it) }.sum() + 1
    }

    override fun getNoc(jClass: JClass): Int {
        return getNodeCount(jClass) - 1
    }

}
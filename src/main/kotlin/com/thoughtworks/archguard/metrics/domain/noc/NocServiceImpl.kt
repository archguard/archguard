package com.thoughtworks.archguard.metrics.domain.noc

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import org.springframework.stereotype.Service

@Service
class NocServiceImpl(val jClassRepository: JClassRepository) : NocService {
    fun getNodeCount(systemId:Long, jClass: JClass): Int {
        val implements = jClassRepository.findClassImplements(systemId, jClass.name, jClass.module)
        if (implements.isEmpty()) {
            return 1
        }
        return implements.map { getNodeCount(systemId, it) }.sum() + 1
    }

    override fun getNoc(systemId: Long, jClass: JClass): Int {
        return getNodeCount(systemId, jClass) - 1
    }

}

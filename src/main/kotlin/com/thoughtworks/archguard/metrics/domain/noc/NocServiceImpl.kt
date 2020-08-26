package com.thoughtworks.archguard.metrics.domain.noc

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import org.springframework.stereotype.Service

@Service
class NocServiceImpl(val jClassRepository: JClassRepository) : NocService {
    fun getNodeCount(projectId:Long, jClass: JClass): Int {
        val implements = jClassRepository.findClassImplements(projectId, jClass.name, jClass.module)
        if (implements.isEmpty()) {
            return 1
        }
        return implements.map { getNodeCount(projectId, it) }.sum() + 1
    }

    override fun getNoc(projectId: Long, jClass: JClass): Int {
        return getNodeCount(projectId, jClass) - 1
    }

}

package com.thoughtworks.archgard.scanner2.domain

import com.thoughtworks.archgard.scanner2.domain.model.JClass
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NocService(val jClassRepository: JClassRepository) {

    private val log = LoggerFactory.getLogger(NocService::class.java)

    fun calculateAllNoc(systemId: Long, jClasses: List<JClass>): Map<String, Int> {
        val nocMap: MutableMap<String, Int> = mutableMapOf()
        jClasses.forEach { nocMap[it.toVO().id!!] = getNoc(systemId, it) }
        log.info("Finish calculate all noc, count: {}", nocMap.keys.size)

        return nocMap
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

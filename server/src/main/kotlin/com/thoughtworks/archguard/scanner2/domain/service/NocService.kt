package com.thoughtworks.archguard.scanner2.domain.service

import com.thoughtworks.archguard.scanner2.domain.model.JClass
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NocService(val jClassRepository: JClassRepository) {

    private val log = LoggerFactory.getLogger(NocService::class.java)

    suspend fun calculate(systemId: Long, jClasses: List<JClass>): Map<String, Int> {
        val nocMap: MutableMap<String, Int> = mutableMapOf()
        jClasses.forEach { nocMap[it.id] = getNoc(systemId, it) }
        log.info("Finish calculate all noc, count: {}", nocMap.keys.size)

        return nocMap
    }

    internal fun getNoc(systemId: Long, jClass: JClass): Int {
        return getNodeCount(systemId, jClass) - 1
    }

    internal fun getNodeCount(systemId: Long, jClass: JClass): Int {
        val implements = jClassRepository.findClassImplements(systemId, jClass.name, jClass.module)
        if (implements.isEmpty()) {
            return 1
        }
        return implements.sumOf { getNodeCount(systemId, it) } + 1
    }
}

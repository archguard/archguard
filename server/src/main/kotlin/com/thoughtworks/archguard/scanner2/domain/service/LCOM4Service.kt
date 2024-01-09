package com.thoughtworks.archguard.scanner2.domain.service

import org.archguard.model.code.JClass
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
import org.archguard.operator.LCOM4Graph
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LCOM4Service(
    val jClassRepository: JClassRepository,
    val jMethodRepository: JMethodRepository
) {

    private val log = LoggerFactory.getLogger(LCOM4Service::class.java)

    suspend fun calculate(systemId: Long, jClasses: List<JClass>): Map<String, Int> {
        jClasses.forEach { prepareJClassBasicDataForLCOM4(systemId, it) }

        val lcom4Map: MutableMap<String, Int> = mutableMapOf()
        jClasses.forEach { lcom4Map[it.id] = LCOM4Graph.buildGraph(it).getConnectivityCount() }
        log.info("Finish calculate all lcom4, count: {}", lcom4Map.keys.size)

        return lcom4Map
    }

    private fun prepareJClassBasicDataForLCOM4(systemId: Long, jClass: JClass) {
        jClass.fields = jClassRepository.findFields(jClass.id)
        val methods = jMethodRepository.findMethodsByModuleAndClass(systemId, jClass.module, jClass.name)
        methods.forEach { it.fields = jMethodRepository.findMethodFields(it.id) }
        methods.forEach { it.callees = jMethodRepository.findMethodCallees(it.id) }
        jClass.methods = methods
    }
}


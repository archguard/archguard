package com.thoughtworks.archgard.scanner2.domain.service.circular

import com.thoughtworks.archgard.scanner2.domain.model.JMethodVO
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MethodCircularProcessor(val jMethodRepository: JMethodRepository,
                              jClassRepository: JClassRepository,
                              circularDependencyMetricRepository: CircularDependencyMetricRepository)
    : CircularProcessor(jClassRepository, circularDependencyMetricRepository) {

    private val log = LoggerFactory.getLogger(MethodCircularProcessor::class.java)

    override fun process(systemId: Long): List<List<JMethodVO>> {
        log.info("Start to find methodCircularDependency in systemId $systemId")
        val methodCircularDependency = getMethodCircularDependency(systemId)
        if (methodCircularDependency.isNotEmpty()) {
            circularDependencyMetricRepository.insertOrUpdateMethodCircularDependency(systemId, methodCircularDependency)
        }
        log.info("Finished persist methodCircularDependency in systemId $systemId")

        return methodCircularDependency
    }

    private fun getMethodCircularDependency(systemId: Long): List<List<JMethodVO>> {
        val allMethodDependencies = jMethodRepository.getDistinctMethodDependenciesAndNotThirdParty(systemId)
        val cycles = findCyclesFromDependencies(allMethodDependencies)
        if (cycles.isEmpty()) {
            return emptyList()
        }
        val methodsHasModules = jMethodRepository.getMethodsNotThirdParty(systemId)
        return cycles.map { it.map { methodsHasModules.first { jMethod -> jMethod.id == it.getNodeId() }.toVO() } }
    }
}
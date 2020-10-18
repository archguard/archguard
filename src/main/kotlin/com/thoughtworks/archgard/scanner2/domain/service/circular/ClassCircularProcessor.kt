package com.thoughtworks.archgard.scanner2.domain.service.circular

import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.infrastructure.Toggle
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ClassCircularProcessor(jClassRepository: JClassRepository,
                             circularDependencyMetricRepository: CircularDependencyMetricRepository)
    : CircularProcessor(jClassRepository, circularDependencyMetricRepository) {

    private val log = LoggerFactory.getLogger(ClassCircularProcessor::class.java)

    override fun process(systemId: Long): List<List<JClassVO>> {
        log.info("Start to find classCircularDependency in systemId $systemId")
        val classCircularDependency = getClassCircularDependency(systemId)
        if (classCircularDependency.isNotEmpty()) {
            circularDependencyMetricRepository.insertOrUpdateClassCircularDependency(systemId, classCircularDependency)
        }
        log.info("Finished persist classCircularDependency in systemId $systemId")

        return classCircularDependency
    }

    private fun getClassCircularDependency(systemId: Long): List<List<JClassVO>> {
        val allClassDependencies = jClassRepository.getDistinctClassDependenciesAndNotThirdParty(systemId)
        val cycles = findCyclesFromDependencies(allClassDependencies)
        val jClassesHasModules = jClassRepository.getJClassesNotThirdPartyAndNotTest(systemId)
        if (cycles.isEmpty()) {
            return emptyList()
        }

        val cycleList = cycles.map { it.map { jClassesHasModules.first { jClass -> jClass.id == it.getNodeId() }.toVO() } }
        return if (Toggle.EXCLUDE_INTERNAL_CLASS_CYCLE_DEPENDENCY.getStatus()) {
            cycleList.filter { cycle -> !isInternalClassCycle(cycle) }
        } else {
            cycleList
        }
    }

    private fun isInternalClassCycle(jClassList: List<JClassVO>): Boolean {
        return when (jClassList.map { it.getBaseClassName() }.toSet().size) {
            1 -> true
            else -> false
        }
    }


}
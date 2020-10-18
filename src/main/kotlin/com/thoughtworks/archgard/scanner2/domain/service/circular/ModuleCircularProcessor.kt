package com.thoughtworks.archgard.scanner2.domain.service.circular

import com.thoughtworks.archgard.scanner2.domain.model.Dependency
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ModuleCircularProcessor(jClassRepository: JClassRepository,
                              circularDependencyMetricRepository: CircularDependencyMetricRepository)
    : CircularProcessor(jClassRepository, circularDependencyMetricRepository) {

    private val log = LoggerFactory.getLogger(ModuleCircularProcessor::class.java)

    override fun process(systemId: Long): List<List<String>> {
        log.info("Start to find moduleCircularDependency in systemId $systemId")
        val moduleCircularDependency = getModuleCircularDependency(systemId)
        if (moduleCircularDependency.isNotEmpty()) {
            circularDependencyMetricRepository.insertOrUpdateModuleCircularDependency(systemId, moduleCircularDependency)
        }
        log.info("Finished persist moduleCircularDependency in systemId $systemId")

        return moduleCircularDependency
    }

    private fun getModuleCircularDependency(systemId: Long): List<List<String>> {
        val moduleDependencies = buildModuleDependencies(systemId)
        val cycles = findCyclesFromDependencies(moduleDependencies)
        if (cycles.isEmpty()) {
            return emptyList()
        }
        return cycles.map { it.map { it.getNodeId() } }
    }

    private fun buildModuleDependencies(systemId: Long): MutableSet<Dependency<String>> {
        val allClassDependencies = buildAllClassDependencies(systemId)
        val moduleDependencies = mutableSetOf<Dependency<String>>()
        allClassDependencies.forEach {
            if (it.caller.module!! != it.callee.module!!) {
                moduleDependencies.add(Dependency(it.caller.module, it.callee.module))
            }
        }
        return moduleDependencies
    }
}
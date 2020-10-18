package com.thoughtworks.archgard.scanner2.domain.service.circular

import com.thoughtworks.archgard.scanner2.domain.model.Dependency
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PackageCircularProcessor(jClassRepository: JClassRepository,
                               circularDependencyMetricRepository: CircularDependencyMetricRepository)
    : CircularProcessor(jClassRepository, circularDependencyMetricRepository) {

    private val log = LoggerFactory.getLogger(PackageCircularProcessor::class.java)

    override fun process(systemId: Long): List<List<String>> {
        log.info("Start to find packageCircularDependency in systemId $systemId")
        val packageCircularDependency = getPackageCircularDependency(systemId)
        if (packageCircularDependency.isNotEmpty()) {
            circularDependencyMetricRepository.insertOrUpdatePackageCircularDependency(systemId, packageCircularDependency)
        }
        log.info("Finished persist packageCircularDependency in systemId $systemId")

        return packageCircularDependency
    }

    private fun getPackageCircularDependency(systemId: Long): List<List<String>> {
        val packageDependencies = buildPackageDependencies(systemId)
        val cycles = findCyclesFromDependencies(packageDependencies)
        if (cycles.isEmpty()) {
            return emptyList()
        }
        return cycles.map { it.map { it.getNodeId() } }
    }

    private fun buildPackageDependencies(systemId: Long): MutableSet<Dependency<String>> {
        val allClassDependencies = buildAllClassDependencies(systemId)
        val packageDependencies = mutableSetOf<Dependency<String>>()
        allClassDependencies.forEach {
            val callerPackageName = "${it.caller.module}.${it.caller.getPackageName()}"
            val calleePackageName = "${it.caller.module}.${it.callee.getPackageName()}"
            if (callerPackageName != calleePackageName) {
                packageDependencies.add(Dependency(callerPackageName, calleePackageName))
            }
        }
        return packageDependencies
    }
}
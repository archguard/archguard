package com.thoughtworks.archguard.scanner2.domain.service

import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
import org.archguard.model.FanInFanOutResult
import org.archguard.model.code.JClass
import org.archguard.operator.FanInFanOutCalculator.buildModuleDependencyFromClassDependency
import org.archguard.operator.FanInFanOutCalculator.buildPackageDependencyFromClassDependency
import org.archguard.operator.FanInFanOutCalculator.calculateFanInFanOutWithDependency
import org.springframework.stereotype.Service

@Service
class FanInFanOutService(val jClassRepository: JClassRepository, val jMethodRepository: JMethodRepository) {
    suspend fun calculateAtClassLevel(systemId: Long): Map<String, FanInFanOutResult> {
        val allClassDependencies = jClassRepository.getAllClassDependenciesAndNotThirdParty(systemId).toSet()
        return calculateFanInFanOutWithDependency(allClassDependencies)
    }

    fun calculateAtMethodLevel(systemId: Long): Map<String, FanInFanOutResult> {
        val allMethodDependencies = jMethodRepository.getAllMethodDependenciesAndNotThirdParty(systemId).toSet()
        return calculateFanInFanOutWithDependency(allMethodDependencies)
    }

    fun calculateAtPackageLevel(systemId: Long, jClasses: List<JClass>): Map<String, FanInFanOutResult> {
        val allClassDependencies = jClassRepository.getAllClassDependenciesAndNotThirdParty(systemId)
        val packageDependencies = buildPackageDependencyFromClassDependency(allClassDependencies, jClasses).toSet()
        return calculateFanInFanOutWithDependency(packageDependencies)
    }

    fun calculateAtModuleLevel(systemId: Long, jClasses: List<JClass>): Map<String, FanInFanOutResult> {
        val allClassDependencies = jClassRepository.getAllClassDependenciesAndNotThirdParty(systemId)
        val moduleDependencies = buildModuleDependencyFromClassDependency(allClassDependencies, jClasses).toSet()
        return calculateFanInFanOutWithDependency(moduleDependencies)
    }

}


package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.model.Dependency
import com.thoughtworks.archgard.scanner2.domain.model.JClass
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FanInFanOutService(val jClassRepository: JClassRepository, val jMethodRepository: JMethodRepository) {
    private val log = LoggerFactory.getLogger(FanInFanOutService::class.java)

    fun calculateAtClassLevel(systemId: Long): Map<String, FanInFanOut> {
        val allClassDependencies = jClassRepository.getAllClassDependenciesAndNotThirdParty(systemId)
        return calculateFanInFanOutWithDependency(allClassDependencies)
    }

    fun calculateAtMethodLevel(systemId: Long): Map<String, FanInFanOut> {
        val allMethodDependencies = jMethodRepository.getAllMethodDependenciesAndNotThirdParty(systemId)
        return calculateFanInFanOutWithDependency(allMethodDependencies)
    }

    fun calculateAtPackageLevel(systemId: Long, jClasses: List<JClass>): Map<String, FanInFanOut> {
        val allClassDependencies = jClassRepository.getAllClassDependenciesAndNotThirdParty(systemId)
        val packageDependencies = buildPackageDependencyFromClassDependency(allClassDependencies, jClasses)
        return calculateFanInFanOutWithDependency(packageDependencies)
    }

    fun calculateAtModuleLevel(systemId: Long, jClasses: List<JClass>): Map<String, FanInFanOut> {
        val allClassDependencies = jClassRepository.getAllClassDependenciesAndNotThirdParty(systemId)
        val moduleDependencies = buildModuleDependencyFromClassDependency(allClassDependencies, jClasses)
        return calculateFanInFanOutWithDependency(moduleDependencies)
    }

    private fun buildModuleDependencyFromClassDependency(classDependencies: List<Dependency<String>>, jClasses: List<JClass>): List<Dependency<String>> {
        return classDependencies.map { classDependency ->
            val callerClass = jClasses.first { it.id == classDependency.caller }.toVO()
            val calleeClass = jClasses.first { it.id == classDependency.callee }.toVO()
            Dependency(callerClass.module, calleeClass.module)
        }
    }

    private fun buildPackageDependencyFromClassDependency(classDependencies: List<Dependency<String>>, jClasses: List<JClass>): List<Dependency<String>> {
        return classDependencies.map { classDependency ->
            val callerClass = jClasses.first { it.id == classDependency.caller }.toVO()
            val calleeClass = jClasses.first { it.id == classDependency.callee }.toVO()
            Dependency(callerClass.module + "-" + callerClass.getPackageName(), calleeClass.module + "-" + calleeClass.getPackageName())
        }
    }

    internal fun calculateFanInFanOutWithDependency(allClassDependencies: List<Dependency<String>>): Map<String, FanInFanOut> {
        val fanInFanOutMap: MutableMap<String, FanInFanOut> = mutableMapOf()
        allClassDependencies.forEach {
            if (fanInFanOutMap.containsKey(it.caller)) {
                val hub = fanInFanOutMap[it.caller]!!
                fanInFanOutMap[it.caller] = FanInFanOut(hub.fanIn, hub.fanOut + 1)
            } else {
                fanInFanOutMap[it.caller] = FanInFanOut(0, 1)
            }
            if (fanInFanOutMap.containsKey(it.callee)) {
                val hub = fanInFanOutMap[it.callee]!!
                fanInFanOutMap[it.callee] = FanInFanOut(hub.fanIn + 1, hub.fanOut)
            } else {
                fanInFanOutMap[it.callee] = FanInFanOut(1, 0)
            }
        }
        return fanInFanOutMap
    }

}

data class FanInFanOut(val fanIn: Int = 0, val fanOut: Int = 0)

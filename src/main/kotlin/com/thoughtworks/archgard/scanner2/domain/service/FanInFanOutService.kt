package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.model.Dependency
import com.thoughtworks.archgard.scanner2.domain.model.JClass
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FanInFanOutService(val jClassRepository: JClassRepository) {
    private val log = LoggerFactory.getLogger(FanInFanOutService::class.java)

    fun calculateAtClassLevel(systemId: Long, jClasses: List<JClass>): Map<String, FanInFanOut> {
        val jClassIdsNotThirdParty = jClasses.map { it.id }
        val allClassDependencies = jClassRepository.getAllClassDependencies(systemId)
        return calculateClassFanInFanOutWithClassDependency(allClassDependencies, jClassIdsNotThirdParty)
    }

    internal fun calculateClassFanInFanOutWithClassDependency(allClassDependencies: List<Dependency<String>>, jClassIdsNotThirdParty: List<String>): Map<String, FanInFanOut> {
        val fanInFanOutMap: MutableMap<String, FanInFanOut> = mutableMapOf()
        allClassDependencies.forEach {
            if (it.caller in jClassIdsNotThirdParty) {
                if (fanInFanOutMap.containsKey(it.caller)) {
                    val hub = fanInFanOutMap[it.caller]!!
                    fanInFanOutMap[it.caller] = FanInFanOut(hub.fanIn, hub.fanOut + 1)
                } else {
                    fanInFanOutMap[it.caller] = FanInFanOut(0, 1)
                }
            }
            if (it.callee in jClassIdsNotThirdParty) {
                if (fanInFanOutMap.containsKey(it.callee)) {
                    val hub = fanInFanOutMap[it.callee]!!
                    fanInFanOutMap[it.callee] = FanInFanOut(hub.fanIn + 1, hub.fanOut)
                } else {
                    fanInFanOutMap[it.callee] = FanInFanOut(1, 0)
                }
            }
        }
        return fanInFanOutMap
    }

}

data class FanInFanOut(val fanIn: Int = 0, val fanOut: Int = 0)

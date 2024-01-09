package org.archguard.operator

import org.archguard.model.Dependency
import org.archguard.model.FanInFanOutResult
import org.archguard.model.code.JClass
import org.archguard.model.vos.JClassVO

object FanInFanOutCalculator {
    fun buildModuleDependencyFromClassDependency(
        classDependencies: Collection<Dependency<String>>,
        jClasses: List<JClass>
    ): List<Dependency<String>> {
        return classDependencies.map { classDependency ->
            val callerClass = JClassVO.fromClass(jClasses.first { it.id == classDependency.caller })
            val calleeClass = JClassVO.fromClass(jClasses.first { it.id == classDependency.callee })
            Dependency(callerClass.module!!, calleeClass.module!!)
        }
    }

    fun buildPackageDependencyFromClassDependency(
        classDependencies: Collection<Dependency<String>>,
        jClasses: List<JClass>
    ): List<Dependency<String>> {
        return classDependencies.map { classDependency ->
            val callerClass = JClassVO.fromClass(jClasses.first { it.id == classDependency.caller })
            val calleeClass = JClassVO.fromClass(jClasses.first { it.id == classDependency.callee })
            Dependency(
                callerClass.module + "." + callerClass.getPackageName(),
                calleeClass.module + "." + calleeClass.getPackageName()
            )
        }
    }

    fun calculateFanInFanOutWithDependency(allClassDependencies: Collection<Dependency<String>>): Map<String, FanInFanOutResult> {
        val fanInFanOutResultMap: MutableMap<String, FanInFanOutResult> = mutableMapOf()
        allClassDependencies.forEach {
            if (fanInFanOutResultMap.containsKey(it.caller)) {
                val hub = fanInFanOutResultMap[it.caller]!!
                fanInFanOutResultMap[it.caller] = FanInFanOutResult(hub.fanIn, hub.fanOut + 1)
            } else {
                fanInFanOutResultMap[it.caller] = FanInFanOutResult(0, 1)
            }
            if (fanInFanOutResultMap.containsKey(it.callee)) {
                val hub = fanInFanOutResultMap[it.callee]!!
                fanInFanOutResultMap[it.callee] = FanInFanOutResult(hub.fanIn + 1, hub.fanOut)
            } else {
                fanInFanOutResultMap[it.callee] = FanInFanOutResult(1, 0)
            }
        }

        return fanInFanOutResultMap
    }
}
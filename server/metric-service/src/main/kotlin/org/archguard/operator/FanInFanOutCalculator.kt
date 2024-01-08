package org.archguard.operator

import org.archguard.model.Dependency
import org.archguard.model.FanInFanOut
import org.archguard.model.code.JClass
import org.archguard.model.vos.JClassVO

object FanInFanOut {
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

    fun calculateFanInFanOutWithDependency(allClassDependencies: Collection<Dependency<String>>): Map<String, FanInFanOut> {
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
package org.archguard.operator

import org.archguard.model.Dependency
import org.archguard.model.FanInFanOut

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
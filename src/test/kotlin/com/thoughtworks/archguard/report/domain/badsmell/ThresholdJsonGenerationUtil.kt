package com.thoughtworks.archguard.report.domain.badsmell

import com.google.gson.Gson
import com.thoughtworks.archguard.report.infrastructure.BadSmellGroupPO
import com.thoughtworks.archguard.report.infrastructure.BadSmellThresholdPO

class ThresholdJsonGenerationUtil {

    fun toThresholdGroup(groups: List<Pair<ThresholdKey, Int>>): BadSmellGroupPO {
        val thresholdPOs: List<BadSmellThresholdPO> = groups.map { BadSmellThresholdPO(it.first, it.second) }
        return BadSmellGroupPO(groups.first().first.dimension, thresholdPOs)
    }

    fun getSizingGroup(): BadSmellGroupPO {
        return toThresholdGroup(
            listOf(
                Pair(ThresholdKey.SIZING_PACKAGE_BY_CLASS_COUNT, 30),
                Pair(ThresholdKey.SIZING_PACKAGE_BY_LOC, 20000),
                Pair(ThresholdKey.SIZING_MODULE_BY_PACKAGE_COUNT, 30),
                Pair(ThresholdKey.SIZING_MODULE_BY_LOC, 40000),
                Pair(ThresholdKey.SIZING_CLASS_BY_FUNC_COUNT, 30),
                Pair(ThresholdKey.SIZING_CLASS_BY_LOC, 2000),
                Pair(ThresholdKey.SIZING_METHOD_BY_LOC, 80)
            )
        )
    }

    fun getCouplingGroup(): BadSmellGroupPO {
        return toThresholdGroup(
            listOf(
                Pair(ThresholdKey.COUPLING_HUB_MODULE, 16),
                Pair(ThresholdKey.COUPLING_HUB_PACKAGE, 16),
                Pair(ThresholdKey.COUPLING_HUB_CLASS, 16),
                Pair(ThresholdKey.COUPLING_HUB_METHOD, 16),
                Pair(ThresholdKey.COUPLING_DATA_CLUMPS, 8),
                Pair(ThresholdKey.COUPLING_DEEP_INHERITANCE, 6),
                Pair(ThresholdKey.COUPLING_CIRCULAR, 0)
            )
        )
    }
}

fun main(args: Array<String>) {
    val util = ThresholdJsonGenerationUtil()
    val groups = listOf(util.getSizingGroup(), util.getCouplingGroup())
    println("----------------------")
    println(Gson().toJson(groups))
    println("----------------------")
}

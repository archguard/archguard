package com.thoughtworks.archguard.report.infrastructure

import com.google.gson.Gson
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuite

class BadSmellSuitePO(val id: Long, val suiteName: String, val thresholds: String, val systemIds: String) {

    fun toBadSmellSuite(): ThresholdSuite {
        val groups = Gson().fromJson(thresholds, Array<BadSmellGroupPO>::class.java).toList()
        val thresholdMapList = groups.flatMap { it.toThresholdMap() }
        return ThresholdSuite(id, suiteName, thresholdMapList,
                systemIds.split(",").map { it.trim().toLong() })
    }
}




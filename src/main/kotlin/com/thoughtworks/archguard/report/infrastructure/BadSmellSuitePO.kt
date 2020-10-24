package com.thoughtworks.archguard.report.infrastructure

import com.google.gson.Gson
import com.thoughtworks.archguard.report.domain.badsmell.FlattenThreshold
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuite

class BadSmellSuitePO(val id: Long, val suiteName: String, val thresholds: String, val systemIds: String) {

    fun toBadSmellSuite(): ThresholdSuite {
        val groups = Gson().fromJson(thresholds, Array<BadSmellGroup>::class.java).toList()
        val thresholdMapList = groups.flatMap { it.toThresholdMap() }
        return ThresholdSuite(id, suiteName, thresholdMapList,
                systemIds.split(",").map { it.toLong() })
    }
}

class BadSmellGroup(val name: String, val threshold: List<BadSmellThreshold>) {
    fun toThresholdMap(): List<FlattenThreshold> {
        return threshold.flatMap { thre ->
            mutableListOf(FlattenThreshold(name, thre.name, thre.condition, thre.value))
        }
    }
}

data class BadSmellThreshold(val name: String, val condition: String, val value: Int)

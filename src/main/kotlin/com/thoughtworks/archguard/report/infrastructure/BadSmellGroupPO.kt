package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.badsmell.FlattenThreshold

class BadSmellGroupPO(val name: String, val threshold: List<BadSmellThresholdPO>) {

    fun toThresholdMap(): List<FlattenThreshold> {
        return threshold.flatMap { thre ->
            mutableListOf(FlattenThreshold(name, thre.name, thre.condition, thre.value))
        }
    }
}

package com.thoughtworks.archguard.report.infrastructure

import org.archguard.threshold.FlattenThreshold

class BadSmellGroupPO(val name: String, val threshold: List<BadSmellThresholdPO>) {

    fun toThresholdMap(): List<FlattenThreshold> {
        return threshold.flatMap { thre ->
            mutableListOf(FlattenThreshold(name, thre.name, thre.condition, thre.value))
        }
    }
}

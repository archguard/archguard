package com.thoughtworks.archguard.report.domain.badsmell

class ThresholdSuite(
    val id: Long,
    val suiteName: String,
    val thresholds: List<FlattenThreshold>,
    val systemIds: List<Long>
) {
    fun getValue(thresholdKey: ThresholdKey): Int {
        return thresholds.first { it.matchKey(thresholdKey.dimension, thresholdKey.key, thresholdKey.condition) }.value
    }
}

package org.archguard.domain.insight

data class InsightModel(
    val condType: InsightConditionType,
    val field: String,
    val comparison: String,
    val value: String,
)
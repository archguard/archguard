package org.archguard.domain.insight

enum class InsightConditionType {
    // can be String
    NORMAL,
    REGEXP,
}

class InsightCondition(
    val type: InsightConditionType = InsightConditionType.REGEXP,
    val value: String = "",
)

fun regexp(value: String): InsightCondition {
    return InsightCondition(InsightConditionType.REGEXP, value)
}


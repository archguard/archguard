package org.archguard.domain.insight

enum class InsightFilterType {
    // can be String
    NORMAL,
    REGEXP,
}

class InsightFieldFilter(
    val type: InsightFilterType = InsightFilterType.REGEXP,
    val value: String = "",
)

fun regexp(value: String): InsightFieldFilter {
    return InsightFieldFilter(InsightFilterType.REGEXP, value)
}


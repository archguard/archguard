package org.archguard.domain.insight

enum class InsightFilterType {
    // can be String
    NORMAL,
    REGEXP,
    LIKE,
}

class InsightFieldFilter(
    var type: InsightFilterType = InsightFilterType.NORMAL,
    var value: String = "",
) {
    fun validate(source: String): Boolean {
        return when(type) {
            InsightFilterType.NORMAL -> source == value
            InsightFilterType.LIKE -> source == value
            InsightFilterType.REGEXP -> source.matches(value.toRegex())
        }
    }
}

fun regexp(value: String): InsightFieldFilter {
    return InsightFieldFilter(InsightFilterType.REGEXP, value)
}


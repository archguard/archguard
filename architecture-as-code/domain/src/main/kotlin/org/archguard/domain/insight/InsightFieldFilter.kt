package org.archguard.domain.insight

enum class InsightFilterType {
    // filter after query
    NORMAL,
    // filter after query
    REGEXP,
    // filter in query
    LIKE,
}

class InsightFieldFilter(
    var type: InsightFilterType = InsightFilterType.NORMAL,
    var value: String = "",
) {
    fun validate(source: String): Boolean {
        return when(type) {
            InsightFilterType.NORMAL -> source == value
            InsightFilterType.REGEXP -> source.matches(value.toRegex())
            else -> false
        }
    }
}


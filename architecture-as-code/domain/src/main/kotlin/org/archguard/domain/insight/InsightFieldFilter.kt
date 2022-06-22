package org.archguard.domain.insight

enum class InsightFilterType {
    // can be String
    NORMAL,
    REGEXP,
}

class InsightFieldFilter(
    val type: InsightFilterType = InsightFilterType.NORMAL,
    val value: String = "",
) {
    fun validate(source: String): Boolean {
        return when(type) {
            InsightFilterType.NORMAL -> source == value
            InsightFilterType.REGEXP -> {
                val regex = value.removeSuffix("/").removePrefix("/").toRegex()
                source.matches(regex)
            }
        }
    }
}

fun regexp(value: String): InsightFieldFilter {
    return InsightFieldFilter(InsightFilterType.REGEXP, value)
}


package org.archguard.domain.insight

import org.archguard.domain.comparison.Comparison

private val ValidInsightRegex = Regex("\\s?([a-zA-Z_]+)\\s?([>!=<]+)\\s?(.*)")


enum class InsightFilterType {
    // filter after query
    NORMAL,
    // filter after query
    REGEXP,
    // filter in query
    LIKE,
}

/**
 *
 */
data class FieldFilter(
    val name: String,
    val value: String,
    val comparison: Comparison = Comparison.NotSupport,
    var type: InsightFilterType = InsightFilterType.NORMAL,
) {
    fun validate(source: String): Boolean {
        return when(type) {
            InsightFilterType.NORMAL -> source == value
            InsightFilterType.REGEXP -> source.matches(value.toRegex())
            else -> false
        }
    }
    
    companion object {
        fun toQuery(models: List<FieldFilter>): String {
            return models.mapNotNull {
                when (it.type) {
                    InsightFilterType.NORMAL -> {
                        when (it.comparison) {
                            Comparison.Equal -> "${it.name} = '${it.value}'"
                            Comparison.NotEqual -> "${it.name} != '${it.value}'"
                            else -> null
                        }
                    }
                    InsightFilterType.LIKE -> {
                        "${it.name} like '${it.value}'"
                    }

                    else -> null
                }
            }.joinToString(" and ")
        }

        fun parse(str: String): List<FieldFilter> {
            return str.split("field:").mapIndexedNotNull { index, it ->
                if (it == "") null

                if (index > 0 && it.endsWith(" ")) {
                    parseOneModel(it.removeSuffix(" "))
                } else {
                    parseOneModel(it)
                }
            }
        }

        private fun parseOneModel(str: String): FieldFilter? {
            if (!ValidInsightRegex.matches(str)) return null

            val matchResult = ValidInsightRegex.find(str)!!.groups

            if (matchResult.size != 4) return null

            val textValue = matchResult[3]!!.value

            var type = InsightFilterType.NORMAL
            var value: String = textValue;

            val isDoubleString = textValue.startsWith("\"") && textValue.endsWith("\"")
            val isSingleString = textValue.startsWith("'") && textValue.endsWith("'")
            val isLikeSearch = textValue.startsWith("%") && textValue.endsWith("%")
            val isRegex = textValue.startsWith("/") && textValue.endsWith("/")

            when {
                isDoubleString -> {
                    value = textValue.removeSurrounding("\"")
                    if (value.startsWith('%') || value.endsWith('%')) {
                        type = InsightFilterType.LIKE
                    }
                }

                isSingleString -> {
                    value = textValue.removeSurrounding("'")
                    if (value.startsWith('%') || value.endsWith('%')) {
                        type = InsightFilterType.LIKE
                    }
                }

                isLikeSearch -> {
                    value = textValue
                    type = InsightFilterType.LIKE
                }

                isRegex -> {
                    type = InsightFilterType.REGEXP
                    value = textValue.removeSurrounding("/")
                }

                else -> {
                    value = textValue
                }
            }

            return FieldFilter(
                name = matchResult[1]!!.value,
                comparison = Comparison.fromString(matchResult[2]!!.value),
                value = value,
                type = type
            )
        }
    }
}

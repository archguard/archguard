package org.archguard.domain.insight

import org.archguard.domain.comparison.Comparison

private val ValidInsightRegex = Regex("\\s?([a-zA-Z_]+)\\s?([>!=<]+)\\s?(.*)")

enum class FilterType {
    // filter after query
    NORMAL,
    // filter after query
    REGEXP,
    // filter in query
    LIKE,
}

typealias FilterValue = String

/**
 *
 */
data class FieldFilter(
    val name: String,
    val value: FilterValue,
    val comparison: Comparison = Comparison.NotSupport,
    var type: FilterType = FilterType.NORMAL,
) {

    companion object {
        /**
         * for filter in query
         * @param operator, sql keyword in query, like `where`, `and`
          */
        fun toQuery(models: List<FieldFilter>, operator: String): String {
            val query = multipleQuery(models)
            if(query.isEmpty()) {
                return ""
            }

            return "$operator $query"
        }

        private fun multipleQuery(models: List<FieldFilter>) = models.mapNotNull {
            when (it.type) {
                FilterType.NORMAL -> {
                    fromComparison(it)
                }

                FilterType.LIKE -> {
                    "${it.name} like '${it.value}'"
                }

                else -> null
            }
        }.joinToString(" and ")

        private fun fromComparison(it: FieldFilter): String? {
            return when (it.comparison) {
                Comparison.Equal -> "${it.name} = '${it.value}'"
                Comparison.NotEqual -> "${it.name} != '${it.value}'"
                else -> null
            }
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

            var type = FilterType.NORMAL
            var value: String = textValue;

            val isDoubleString = textValue.startsWith("\"") && textValue.endsWith("\"")
            val isSingleString = textValue.startsWith("'") && textValue.endsWith("'")
            val isLikeSearch = textValue.startsWith("%") && textValue.endsWith("%")
            val isRegex = textValue.startsWith("/") && textValue.endsWith("/")

            when {
                isDoubleString -> {
                    value = textValue.removeSurrounding("\"")
                    if (value.startsWith('%') || value.endsWith('%')) {
                        type = FilterType.LIKE
                    }
                }

                isSingleString -> {
                    value = textValue.removeSurrounding("'")
                    if (value.startsWith('%') || value.endsWith('%')) {
                        type = FilterType.LIKE
                    }
                }

                isLikeSearch -> {
                    value = textValue
                    type = FilterType.LIKE
                }

                isRegex -> {
                    type = FilterType.REGEXP
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

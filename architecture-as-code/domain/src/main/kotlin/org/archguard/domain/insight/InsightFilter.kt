package org.archguard.domain.insight

/**
 * @param symbol in `==`、`>`、`<`、`>=`、`<=`、`!=`
 * @param value in number or string
 */
data class InsightValueExpr(
    val symbol: String,
    val value: String,
)

private val ValidInsightRegex = Regex("\\s?([a-zA-Z_]+)\\s?([>!=<]+)\\s?(.*)")

/**
 * @param field is field name
 *
 */
data class InsightFilter(
    val field: String,
    val filter: InsightFieldFilter,
    val valueExpr: InsightValueExpr,
) {
    companion object {
        fun toQuery(models: List<InsightFilter>): String {
            return models.mapNotNull {
                when (it.filter.type) {
                    InsightFilterType.NORMAL -> {
                        when (it.valueExpr.symbol) {
                            "==" -> "${it.field} = '${it.filter.value}'"
                            "!=" -> "${it.field} != '${it.filter.value}'"
                            else -> null
                        }
                    }

                    InsightFilterType.LIKE -> {
                        "${it.field} like '${it.filter.value}'"
                    }

                    else -> null
                }
            }.joinToString(" and ")
        }

        fun parse(str: String): List<InsightFilter> {
            return str.split("field:").mapIndexedNotNull { index, it ->
                if (it == "") null

                if (index > 0 && it.endsWith(" ")) {
                    parseOneModel(it.removeSuffix(" "))
                } else {
                    parseOneModel(it)
                }
            }
        }

        private fun parseOneModel(str: String): InsightFilter? {
            if (!ValidInsightRegex.matches(str)) return null

            val matchResult = ValidInsightRegex.find(str)!!.groups

            if (matchResult.size != 4) return null

            val textValue = matchResult[3]!!.value

            val fieldFilter = InsightFieldFilter()

            val isDoubleString = textValue.startsWith("\"") && textValue.endsWith("\"")
            val isSingleString = textValue.startsWith("'") && textValue.endsWith("'")
            val isLikeSearch = textValue.startsWith("%") && textValue.endsWith("%")
            val isRegex = textValue.startsWith("/") && textValue.endsWith("/")

            when {
                isDoubleString -> {
                    fieldFilter.value = textValue.removeSurrounding("\"")
                    if (fieldFilter.value.startsWith('%') || fieldFilter.value.endsWith('%')) {
                        fieldFilter.type = InsightFilterType.LIKE
                    }
                }

                isSingleString -> {
                    fieldFilter.value = textValue.removeSurrounding("'")
                    if (fieldFilter.value.startsWith('%') || fieldFilter.value.endsWith('%')) {
                        fieldFilter.type = InsightFilterType.LIKE
                    }
                }

                isLikeSearch -> {
                    fieldFilter.value = textValue
                    fieldFilter.type = InsightFilterType.LIKE
                }

                isRegex -> {
                    fieldFilter.type = InsightFilterType.REGEXP
                    fieldFilter.value = textValue.removeSurrounding("/")
                }

                else -> {
                    fieldFilter.value = textValue
                }
            }

            return InsightFilter(
                matchResult[1]!!.value,
                fieldFilter,
                InsightValueExpr(matchResult[2]!!.value, textValue)
            )
        }
    }
}

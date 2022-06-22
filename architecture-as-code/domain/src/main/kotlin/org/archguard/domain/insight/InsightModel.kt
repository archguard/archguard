package org.archguard.domain.insight

data class InsightValueExpr(
    val comparison: String,
    val value: String,
)

private val ValidInsightRegex = Regex("\\s?([a-zA-Z_]+)\\s?([>!=<]+)\\s?(.*)")

data class InsightModel(
    val field: String,
    val fieldFilter: InsightFieldFilter,
    val valueExpr: InsightValueExpr,
) {
    companion object {
        fun parse(str: String): List<InsightModel> {
            return str.split("field:").mapIndexedNotNull { index, it ->
                if (index > 0 && it.endsWith(" ")) {
                    parseOneModel(it.removeSuffix(" "))
                } else {
                    parseOneModel(it)
                }
            }
        }

        private fun parseOneModel(str: String): InsightModel? {
            if (!ValidInsightRegex.matches(str)) return null

            val matchResult = ValidInsightRegex.find(str)!!.groups

            if (matchResult.size != 4) return null

            val textValue = matchResult[3]!!.value

            val fieldFilter = InsightFieldFilter()

            val isDoubleString = textValue.startsWith("\"") && textValue.endsWith("\"")
            val isSingleString = textValue.startsWith("'") && textValue.endsWith("'")
            val isRegex = textValue.startsWith("/") && textValue.endsWith("/")

            when {
                isDoubleString -> {
                    fieldFilter.value = textValue.removeSurrounding("\"")
                }
                isSingleString -> {
                    fieldFilter.value = textValue.removeSurrounding("'")
                }
                isRegex -> {
                    fieldFilter.type = InsightFilterType.REGEXP
                    fieldFilter.value = textValue.removeSurrounding("/")
                }
                else -> {
                    fieldFilter.value = textValue
                }
            }

            return InsightModel(
                matchResult[1]!!.value,
                fieldFilter,
                InsightValueExpr(matchResult[2]!!.value, textValue)
            )
        }
    }
}

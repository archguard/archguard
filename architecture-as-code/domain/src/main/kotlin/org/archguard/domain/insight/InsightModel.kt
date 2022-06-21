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

            return InsightModel(
                matchResult[1]!!.value,
                InsightFieldFilter(),
                InsightValueExpr(matchResult[2]!!.value, matchResult[3]!!.value)
            )
        }
    }
}

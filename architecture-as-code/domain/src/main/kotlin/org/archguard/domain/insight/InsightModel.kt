package org.archguard.domain.insight

data class InsightValueExpr(
    val comparison: String,
    val value: String,
)

private val ValidInsightRegex = Regex("([a-zA-Z_]+):([a-zA-Z_]+)\\s+([>!=<]+)\\s+(.*)")

data class InsightModel(
    val field: String,
    val fieldFilter: InsightFieldFilter,
    val valueExpr: InsightValueExpr,
) {
    companion object {
        fun parse(str: String): InsightModel? {
            if (!ValidInsightRegex.matches(str)) return null

            val matchResult = ValidInsightRegex.find(str)!!.groups

            if (matchResult.size != 5) return null

            if (matchResult[1]!!.value != "field") {
                return null
            }

            return InsightModel(
                matchResult[2]!!.value,
                InsightFieldFilter(),
                InsightValueExpr(matchResult[3]!!.value, matchResult[4]!!.value)
            )
        }
    }
}

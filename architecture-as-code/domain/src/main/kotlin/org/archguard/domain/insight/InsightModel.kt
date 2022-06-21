package org.archguard.domain.insight

data class InsightValueExpr(
    val comparison: String,
    val value: String,
)

class InsightParser(private val text: String) {
    private val length = text.length
    private var pos = 0


}

private val ValidInsightRegex = Regex("([a-zA-Z_]+):([a-zA-Z_]+)\\s+([>!=<]+)\\s+(.*)")

data class InsightModel(
    val field: String,
    val fieldFilter: InsightFieldFilter,
    val valueExpr: InsightValueExpr,
) {

    companion object {
        fun parse(str: String): InsightModel? {
            if(!ValidInsightRegex.matches(str)) return null

            return InsightModel(
                "",
                InsightFieldFilter(),
                InsightValueExpr("", "")
            )
        }
    }
}

package com.thoughtworks.archguard.insights.application.sca

import com.thoughtworks.archguard.insights.application.InsightModelDto
import org.archguard.domain.comparison.Comparison
import org.archguard.domain.insight.*
import org.archguard.domain.version.VersionComparison

object ScaInsightFilter {
    fun byInsight(
        query: Query,
        insightModelDtos: List<InsightModelDto>,
    ): List<InsightModelDto> {
        val versionComparison = VersionComparison()
        var versionFilter: Pair<FilterValue, Comparison>? = null
        var nameFilter: Pair<FilterValue, QueryMode>? = null

        query.data.map { filter ->
            when (filter.getLeftOrNull()?.left) {
                "dep_version" -> {
                    val expr = filter.getLeftOrNull()!!
                    versionFilter = expr.right to expr.comparison
                }

                "dep_name" -> {
                    val expr = filter.getLeftOrNull()!!
                    val isFilterInQuery = expr.queryMode == QueryMode.LikeMode
                    if (!isFilterInQuery) {
                        nameFilter = expr.right to expr.queryMode
                    }
                }

                else -> {}
            }
        }


        val filteredModels = insightModelDtos.filter {
            ValueValidate.isVersionValid(it.dep_version, versionComparison, versionFilter)
                    && ValueValidate.isValueValid(it.dep_name, nameFilter)
        }

        return filteredModels
    }
}


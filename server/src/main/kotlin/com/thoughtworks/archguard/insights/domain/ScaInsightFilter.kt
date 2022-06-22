package com.thoughtworks.archguard.insights.domain

import org.archguard.domain.insight.InsightFieldFilter
import org.archguard.domain.insight.InsightModel
import org.archguard.domain.version.VersionComparison

object ScaInsightFilter {
    private fun isNameValid(nameFilter: InsightFieldFilter?, name: String): Boolean {
        if (nameFilter == null) return true

        return nameFilter.validate(name)
    }

    private fun isVersionValid(
        versionComparison: VersionComparison,
        versionFilter: Pair<String, String>?,
        leftVersion: String,
    ): Boolean {
        if (versionFilter == null) {
            return true
        }

        val comparison = versionFilter.second
        val insightVersion = versionFilter.first
        return versionComparison.eval(leftVersion, comparison, insightVersion)
    }

    fun filterByInsight(
        models: List<InsightModel>,
        scaModelDtos: List<ScaModelDto>,
    ): List<ScaModelDto> {
        val versionComparison = VersionComparison()
        var versionFilter: Pair<String, String>? = null
        var nameFilter: InsightFieldFilter? = null

        models.map { insight ->
            when (insight.field) {
                "version" -> {
                    versionFilter = insight.valueExpr.value to insight.valueExpr.comparison
                }

                "name" -> {
                    nameFilter = InsightFieldFilter(insight.fieldFilter.type, insight.fieldFilter.value)
                }

                else -> {}
            }
        }


        val filteredModels = scaModelDtos.filter {
            isVersionValid(versionComparison, versionFilter, it.dep_version) &&
                    isNameValid(nameFilter, it.dep_name)
        }

        return filteredModels
    }
}
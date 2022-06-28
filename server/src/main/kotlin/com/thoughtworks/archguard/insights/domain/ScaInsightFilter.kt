package com.thoughtworks.archguard.insights.domain

import org.archguard.domain.comparison.Comparison
import org.archguard.domain.insight.InsightFilterType
import org.archguard.domain.insight.FieldFilter
import org.archguard.domain.version.VersionComparison

object ScaInsightFilter {
    private fun validate(source: String, type: InsightFilterType, value: String): Boolean {
        return when(type) {
            InsightFilterType.NORMAL -> source == value
            InsightFilterType.REGEXP -> source.matches(value.toRegex())
            else -> false
        }
    }

    private fun isValueValid(source: String, nameFilter: Pair<String, InsightFilterType>?): Boolean {
        if (nameFilter == null) return true

        return validate(source, nameFilter.second, nameFilter.first)
    }

    private fun isVersionValid(
        versionComparison: VersionComparison,
        versionFilter: Pair<String, Comparison>?,
        leftVersion: String,
    ): Boolean {
        if (versionFilter == null) {
            return true
        }

        val comparison = versionFilter.second
        val insightVersion = versionFilter.first
        return versionComparison.evalByComp(leftVersion, comparison, insightVersion)
    }

    fun filterByInsight(
        filter: List<FieldFilter>,
        scaModelDtos: List<ScaModelDto>,
    ): List<ScaModelDto> {
        val versionComparison = VersionComparison()
        var versionFilter: Pair<String, Comparison>? = null
        var nameFilter: Pair<String, InsightFilterType>? = null

        filter.map { insight ->
            when (insight.name) {
                "dep_version" -> {
                    versionFilter = insight.value to insight.comparison
                }
                "dep_name" -> {
                    val isFilterInQuery = insight.type == InsightFilterType.LIKE
                    if (!isFilterInQuery) {
                        nameFilter = insight.value to insight.type
                    }
                }
                else -> {}
            }
        }


        val filteredModels = scaModelDtos.filter {
            isVersionValid(versionComparison, versionFilter, it.dep_version) && isValueValid(it.dep_name, nameFilter)
        }

        return filteredModels
    }
}
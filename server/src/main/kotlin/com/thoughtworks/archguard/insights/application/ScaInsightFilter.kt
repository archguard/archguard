package com.thoughtworks.archguard.insights.application

import org.archguard.domain.comparison.Comparison
import org.archguard.domain.insight.FilterType
import org.archguard.domain.insight.FieldFilter
import org.archguard.domain.insight.FilterValue
import org.archguard.domain.insight.ValValidate
import org.archguard.domain.version.VersionComparison

object ScaInsightFilter {
    fun filterByInsight(
        filters: List<FieldFilter>,
        insightModelDtos: List<InsightModelDto>,
    ): List<InsightModelDto> {
        val versionComparison = VersionComparison()
        var versionFilter: Pair<FilterValue, Comparison>? = null
        var nameFilter: Pair<FilterValue, FilterType>? = null

        filters.map { filter ->
            when (filter.name) {
                "dep_version" -> {
                    versionFilter = filter.value to filter.comparison
                }

                "dep_name" -> {
                    val isFilterInQuery = filter.type == FilterType.LIKE
                    if (!isFilterInQuery) {
                        nameFilter = filter.value to filter.type
                    }
                }

                else -> {}
            }
        }


        val filteredModels = insightModelDtos.filter {
            ValValidate.isVersionValid(it.dep_version, versionComparison, versionFilter)
                    && ValValidate.isValueValid(it.dep_name, nameFilter)
        }

        return filteredModels
    }
}


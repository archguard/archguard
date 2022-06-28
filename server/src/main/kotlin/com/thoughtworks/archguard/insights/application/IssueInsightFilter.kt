package com.thoughtworks.archguard.insights.application

import org.archguard.domain.insight.FilterType
import org.archguard.domain.insight.FieldFilter
import org.archguard.domain.insight.FilterValue
import org.archguard.domain.insight.ValueValidate

object IssueInsightFilter {
    fun byInsight(filters: List<FieldFilter>, models: List<IssueModelDto>, ): List<IssueModelDto> {
        var ruleType: Pair<FilterValue, FilterType>? = null
        var severityFilter: Pair<FilterValue, FilterType>? = null

        filters.map { filter ->
            when (filter.name) {
                "severity" -> {
                    val isFilterInQuery = filter.type == FilterType.LIKE
                    if (!isFilterInQuery) {
                        severityFilter = filter.value to filter.type
                    }
                }
                "rule_type" -> {
                    val isFilterInQuery = filter.type == FilterType.LIKE
                    if (!isFilterInQuery) {
                        ruleType = filter.value to filter.type
                    }
                }
                else -> {}
            }
        }


        val filteredModels = models.filter {
            ValueValidate.isValueValid(it.rule_type, ruleType) &&
            ValueValidate.isValueValid(it.severity, severityFilter)
        }

        return filteredModels
    }
}


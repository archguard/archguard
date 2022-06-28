package com.thoughtworks.archguard.insights.application

import org.archguard.domain.insight.FilterType
import org.archguard.domain.insight.FieldFilter
import org.archguard.domain.insight.FilterValue
import org.archguard.domain.insight.ValueValidate

object IssueInsightFilter {
    fun byInsight(filters: List<FieldFilter>, models: List<IssueModelDto>, ): List<IssueModelDto> {
        val filterMap: MutableMap<String, Pair<FilterValue, FilterType>> = mutableMapOf()

        filters.map { filter ->
            when (filter.name) {
                "name" ,
                "severity",
                "rule_type" -> {
                    val isFilterInQuery = filter.type == FilterType.LIKE
                    if (!isFilterInQuery) {
                        filterMap[filter.name] = filter.value to filter.type
                    }
                }
                else -> {}
            }
        }

        val filteredModels = models.filter {
            ValueValidate.isValueValid(it.rule_type, filterMap["rule_type"]) &&
            ValueValidate.isValueValid(it.severity, filterMap["severity"]) &&
            ValueValidate.isValueValid(it.name, filterMap["name"])
        }

        return filteredModels
    }
}


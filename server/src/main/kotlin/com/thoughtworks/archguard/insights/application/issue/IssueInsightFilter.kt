package com.thoughtworks.archguard.insights.application.issue

import com.thoughtworks.archguard.insights.application.IssueModelDto
import org.archguard.domain.insight.*

object IssueInsightFilter {
    fun byInsight(filters: Query, models: List<IssueModelDto>): List<IssueModelDto> {
        val filterMap: MutableMap<String, Pair<FilterValue, QueryMode>> = mutableMapOf()

        filters.data.map { filter ->
            when (filter.getLeftOrNull()?.left) {
                "name" ,
                "severity",
                "rule_type" -> {
                    val expr = filter.getLeftOrNull()!!
                    val isFilterInQuery = expr.queryMode == QueryMode.LikeMode
                    if (!isFilterInQuery) {
                        filterMap[expr.left] = expr.right to expr.queryMode
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


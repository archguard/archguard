package com.thoughtworks.archguard.insights.application.sca

import com.thoughtworks.archguard.insights.application.InsightModelDto
import com.thoughtworks.archguard.insights.application.postFilter
import org.archguard.domain.insight.Query
import org.archguard.domain.insight.RegexQuery

object ScaInsightFilter {
    fun byInsight(query: Query, models: List<InsightModelDto>): List<InsightModelDto> {
        return postFilter(query, models, ::filterModel)
    }

    private fun filterModel(
        data: InsightModelDto,
        condition: RegexQuery
    ) = when (condition.field) {
        "dep_name" -> data.dep_name.matches(condition.regex)
        "dep_version" -> data.dep_version.matches(condition.regex)
        "dep_artifact" -> data.dep_artifact.matches(condition.regex)
        "dep_group" -> data.dep_group.matches(condition.regex)
        else -> true
    }
}


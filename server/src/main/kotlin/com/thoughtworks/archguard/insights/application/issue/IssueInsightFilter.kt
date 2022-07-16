package com.thoughtworks.archguard.insights.application.issue

import com.thoughtworks.archguard.insights.application.IssueModelDto
import com.thoughtworks.archguard.insights.application.postFilter
import org.archguard.domain.insight.Query
import org.archguard.domain.insight.RegexQuery

object IssueInsightFilter {
    fun byInsight(query: Query, models: List<IssueModelDto>): List<IssueModelDto> {
        return postFilter(query, models, ::filterModel)
    }

    private fun filterModel(
        data: IssueModelDto,
        condition: RegexQuery
    ) = when (condition.field) {
        "name" -> data.name.matches(condition.regex)
        "severity" -> data.severity.matches(condition.regex)
        "rule_id" -> data.rule_id.matches(condition.regex)
        "rule_type" -> data.rule_type.matches(condition.regex)
        else -> true
    }
}


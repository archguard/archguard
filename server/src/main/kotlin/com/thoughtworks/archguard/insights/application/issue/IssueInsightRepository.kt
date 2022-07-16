package com.thoughtworks.archguard.insights.application.issue

import com.thoughtworks.archguard.insights.application.IssueModelDto
import org.archguard.domain.insight.Query

interface IssueInsightRepository {
    abstract fun filterByConditionWithSystemId(id: Long, query: Query): List<IssueModelDto>
    abstract fun filterByCondition(query: Query): List<IssueModelDto>
}

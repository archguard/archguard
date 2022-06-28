package com.thoughtworks.archguard.insights.application

import org.archguard.domain.insight.FieldFilter

interface IssueInsightRepository {
    abstract fun filterByConditionWithSystemId(id: Long, models: List<FieldFilter>): List<IssueModelDto>
    abstract fun filterByCondition(models: List<FieldFilter>): List<IssueModelDto>
}

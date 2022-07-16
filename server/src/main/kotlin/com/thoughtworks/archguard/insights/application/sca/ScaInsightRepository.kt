package com.thoughtworks.archguard.insights.application.sca

import com.thoughtworks.archguard.insights.application.InsightModelDto
import org.archguard.domain.insight.Query

interface ScaInsightRepository {
    abstract fun filterByConditionWithSystemId(id: Long, query: Query): List<InsightModelDto>
    abstract fun filterByCondition(query: Query): List<InsightModelDto>
}

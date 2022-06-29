package com.thoughtworks.archguard.insights.application.sca

import com.thoughtworks.archguard.insights.application.InsightModelDto
import org.archguard.domain.insight.FieldFilter

interface ScaInsightRepository {
    abstract fun filterByConditionWithSystemId(id: Long, models: List<FieldFilter>): List<InsightModelDto>
    abstract fun filterByCondition(models: List<FieldFilter>): List<InsightModelDto>
}

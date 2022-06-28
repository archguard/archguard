package com.thoughtworks.archguard.insights.application

import org.archguard.domain.insight.FieldFilter

interface ScaInsightRepository {
    abstract fun filterByConditionWithSystemId(id: Long, models: List<FieldFilter>): List<ScaModelDto>
    abstract fun filterByCondition(models: List<FieldFilter>): List<ScaModelDto>
}

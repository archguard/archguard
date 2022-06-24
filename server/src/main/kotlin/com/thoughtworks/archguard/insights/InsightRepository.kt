package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.ScaModelDto

interface InsightRepository {
    abstract fun filterByConditionWithSystemId(id: Long): List<ScaModelDto>
    abstract fun filterByCondition(): List<ScaModelDto>
    abstract fun saveInsight(insight: CustomInsight): Long
    abstract fun getInsightByName(name: String): CustomInsight?
    abstract fun deleteInsightByName(name: String): Int
}

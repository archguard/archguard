package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.CustomInsight

interface InsightRepository {
    abstract fun saveInsight(insight: CustomInsight): Long
    abstract fun getInsightByName(name: String): CustomInsight?
    abstract fun deleteInsightByName(name: String): Int
}

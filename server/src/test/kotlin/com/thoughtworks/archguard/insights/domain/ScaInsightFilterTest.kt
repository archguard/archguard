package com.thoughtworks.archguard.insights.domain

import org.archguard.domain.insight.InsightModel
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ScaInsightFilterTest {
    @Test
    internal fun should_filter_version_by_condition() {
        val insights = InsightModel.parse("field:name == /.*dubbo/ field:version > 1.12.3")
        val dtos = listOf(
            ScaModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.2"),
            ScaModelDto("spring", "spring", "spring", "1.12.4"),
            ScaModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.4"),
        )

        assertEquals(1, ScaInsightFilter.filterByInsight(insights, dtos).size)
    }
}
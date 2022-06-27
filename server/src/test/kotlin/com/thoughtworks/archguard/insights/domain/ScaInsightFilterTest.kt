package com.thoughtworks.archguard.insights.domain

import org.archguard.domain.insight.InsightModel
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ScaInsightFilterTest {
    @Test
    internal fun filter_version_by_condition() {
        val insights = InsightModel.parse("field:dep_name == /.*dubbo/ field:dep_version > 1.12.3")
        val dtos = listOf(
            ScaModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.2"),
            ScaModelDto("spring", "spring", "spring", "1.12.4"),
            ScaModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.4"),
        )

        assertEquals(1, ScaInsightFilter.filterByInsight(insights, dtos).size)
    }

    @Test
    internal fun skip_filter_when_version_empty() {
        val insights = InsightModel.parse("field:dep_name == /.*/ ")
        val dtos = listOf(
            ScaModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.2"),
            ScaModelDto("spring", "spring", "spring", "1.12.4"),
            ScaModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.4"),
        )

        assertEquals(3, ScaInsightFilter.filterByInsight(insights, dtos).size)
    }

    @Test
    internal fun skip_filter_in_query() {
        val insights = InsightModel.parse("field:dep_name == %dubbo% ")
        val dtos = listOf(
            ScaModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.2"),
            ScaModelDto("spring", "spring", "spring", "1.12.4"),
            ScaModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.4"),
        )

        assertEquals(3, ScaInsightFilter.filterByInsight(insights, dtos).size)
    }
}
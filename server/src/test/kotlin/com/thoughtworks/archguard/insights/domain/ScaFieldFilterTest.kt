package com.thoughtworks.archguard.insights.domain

import com.thoughtworks.archguard.insights.application.ScaInsightFilter
import com.thoughtworks.archguard.insights.application.InsightModelDto
import org.archguard.domain.insight.FieldFilter
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ScaFieldFilterTest {
    @Test
    internal fun filter_version_by_condition() {
        val insights = FieldFilter.parse("field:dep_name == /.*dubbo/ field:dep_version > 1.12.3")
        val dtos = listOf(
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.2"),
            InsightModelDto("spring", "spring", "spring", "1.12.4"),
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.4"),
        )

        assertEquals(1, ScaInsightFilter.filterByInsight(insights, dtos).size)
    }

    @Test
    internal fun skip_filter_when_version_empty() {
        val insights = FieldFilter.parse("field:dep_name == /.*/ ")
        val dtos = listOf(
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.2"),
            InsightModelDto("spring", "spring", "spring", "1.12.4"),
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.4"),
        )

        assertEquals(3, ScaInsightFilter.filterByInsight(insights, dtos).size)
    }

    @Test
    internal fun skip_filter_in_query() {
        val insights = FieldFilter.parse("field:dep_name == %dubbo% ")
        val dtos = listOf(
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.2"),
            InsightModelDto("spring", "spring", "spring", "1.12.4"),
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.4"),
        )

        assertEquals(3, ScaInsightFilter.filterByInsight(insights, dtos).size)
    }
}
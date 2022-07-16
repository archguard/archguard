package com.thoughtworks.archguard.insights.application

import com.thoughtworks.archguard.insights.application.sca.ScaInsightFilter
import org.archguard.domain.insight.InsightsParser
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ScaFieldFilterTest {
    // TODO(CGQAQ): Supporting verison in postqueries as well
    @Test
    fun filter_version_by_condition() {
        // Postqueries only
        val insights = InsightsParser.parse("dep_name = /.*dubbo/ and dep_version > 1.12.3")
        val dtos = listOf(
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.2"),
            InsightModelDto("spring", "spring", "spring", "1.12.4"),
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.4"),
        )

        assertEquals(1, ScaInsightFilter.byInsight(insights, dtos).size)
    }

    @Test
    fun skip_filter_when_version_empty() {
        // post query only
        val insights = InsightsParser.parse("dep_name == /.*/ ")
        val dtos = listOf(
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.2"),
            InsightModelDto("spring", "spring", "spring", "1.12.4"),
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.4"),
        )

        assertEquals(3, ScaInsightFilter.byInsight(insights, dtos).size)
    }

    @Test
    fun skip_filter_in_query() {
        // sql query only
        val insights = InsightsParser.parse("dep_name == @%dubbo%@")
        val dtos = listOf(
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.2"),
            InsightModelDto("spring", "spring", "spring", "1.12.4"),
            InsightModelDto("org.apache.dubbo:dubbo", "org.apache.dubbo", "dubbo", "1.12.4"),
        )

        assertEquals(3, ScaInsightFilter.byInsight(insights, dtos).size)
    }
}
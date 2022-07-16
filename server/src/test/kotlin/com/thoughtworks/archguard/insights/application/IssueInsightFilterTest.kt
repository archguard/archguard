package com.thoughtworks.archguard.insights.application

import com.thoughtworks.archguard.insights.application.issue.IssueInsightFilter
import org.archguard.domain.insight.InsightsParser
import org.junit.jupiter.api.Test

internal class IssueInsightFilterTest {

    @Test
    fun filter_version_by_condition() {
        val insights = InsightsParser.parse("severity == /HINT/")
        val dtos = listOf(
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","HINT"),
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","WARN"),
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","WARN"),
        )

        kotlin.test.assertEquals(1, IssueInsightFilter.byInsight(insights, dtos).size)
    }

    @Test
    fun filter_by_name() {
        val insights = InsightsParser.parse("name == /AA/")
        val dtos = listOf(
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","HINT"),
            IssueModelDto("AA", "1", "TEST_CODE_SMELL","WARN"),
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","WARN"),
        )

        kotlin.test.assertEquals(1, IssueInsightFilter.byInsight(insights, dtos).size)
    }

    @Test
    fun filter_by_rule_type() {
        val insights = InsightsParser.parse("rule_type == /AAA/")
        val dtos = listOf(
            IssueModelDto("sample", "1", "AAA","HINT"),
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","WARN"),
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","WARN"),
        )

        kotlin.test.assertEquals(1, IssueInsightFilter.byInsight(insights, dtos).size)
    }
}

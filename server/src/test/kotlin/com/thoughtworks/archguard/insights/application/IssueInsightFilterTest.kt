package com.thoughtworks.archguard.insights.application

import com.thoughtworks.archguard.insights.application.issue.IssueInsightFilter
import org.archguard.domain.insight.FieldFilter
import org.junit.jupiter.api.Test

internal class IssueInsightFilterTest {

    @Test
    internal fun filter_version_by_condition() {
        val insights = FieldFilter.parse("field:severity == 'HINT'")
        val dtos = listOf(
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","HINT"),
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","WARN"),
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","WARN"),
        )

        kotlin.test.assertEquals(1, IssueInsightFilter.byInsight(insights, dtos).size)
    }

    @Test
    internal fun filter_by_name() {
        val insights = FieldFilter.parse("field:name == 'AA'")
        val dtos = listOf(
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","HINT"),
            IssueModelDto("AA", "1", "TEST_CODE_SMELL","WARN"),
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","WARN"),
        )

        kotlin.test.assertEquals(1, IssueInsightFilter.byInsight(insights, dtos).size)
    }

    @Test
    internal fun filter_by_rule_type() {
        val insights = FieldFilter.parse("field:rule_type == 'AAA'")
        val dtos = listOf(
            IssueModelDto("sample", "1", "AAA","HINT"),
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","WARN"),
            IssueModelDto("sample", "1", "TEST_CODE_SMELL","WARN"),
        )

        kotlin.test.assertEquals(1, IssueInsightFilter.byInsight(insights, dtos).size)
    }
}

package com.thoughtworks.archguard.insights.application

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
}

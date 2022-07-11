package com.thoughtworks.archguard.insights.application

import com.thoughtworks.archguard.insights.application.issue.IssueInsightRepository
import com.thoughtworks.archguard.insights.application.sca.ScaInsightRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InsightApplicationServiceTest {

    @MockK
    private lateinit var issueRepo: IssueInsightRepository

    @MockK
    private lateinit var scaRepo: ScaInsightRepository

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun should_return_result_for_filter_after_query() {
        val models = listOf(
            InsightModelDto("CGQAQ", "", "", "")
        )
        every { issueRepo.filterByCondition(any()) }.returns(listOf())
        every { scaRepo.filterByCondition(any()) }.returns(models)

        val insightApplicationService = InsightApplicationService(issueRepo, scaRepo)
        val insight = insightApplicationService.byExpression(null, "dep_name = /CGQAQ/", "sca");

        assertEquals(1, insight.size)
    }
}
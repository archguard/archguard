package com.thoughtworks.archguard.insights.application

import com.thoughtworks.archguard.insights.application.issue.IssueInsightFilter
import com.thoughtworks.archguard.insights.application.issue.IssueInsightRepository
import com.thoughtworks.archguard.insights.application.sca.ScaInsightFilter
import com.thoughtworks.archguard.insights.application.sca.ScaInsightRepository
import org.archguard.domain.insight.FieldFilter
import org.springframework.stereotype.Service

@Service
class InsightApplicationService(
    val issueRepo: IssueInsightRepository,
    val scaRepo: ScaInsightRepository
) {
    fun byExpression(id: Long?, expression: String, type: String): InsightModel {
        val models = FieldFilter.parse(expression)

        return when (type) {
            "sca" -> {
                val insightModelDtos = if (id == null || id == 0L) {
                    scaRepo.filterByCondition(models)
                } else {
                    scaRepo.filterByConditionWithSystemId(id, models)
                }

                val filtered = ScaInsightFilter.byInsight(models, insightModelDtos)
                InsightModel(filtered.size, filtered)
            }
            "issue" -> {
                val issueModelDtos = if (id == null || id == 0L) {
                    issueRepo.filterByCondition(models)
                } else {
                    issueRepo.filterByConditionWithSystemId(id, models)
                }

                val filtered = IssueInsightFilter.byInsight(models, issueModelDtos)
                InsightModel(filtered.size, filtered)
            }
            else -> {
                InsightModel(0, listOf())
            }
        }
    }
}

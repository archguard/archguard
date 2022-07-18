package com.thoughtworks.archguard.insights.application

import com.thoughtworks.archguard.insights.application.issue.IssueInsightFilter
import com.thoughtworks.archguard.insights.application.issue.IssueInsightRepository
import com.thoughtworks.archguard.insights.application.sca.ScaInsightFilter
import com.thoughtworks.archguard.insights.application.sca.ScaInsightRepository
import com.thoughtworks.archguard.insights.application.structure.StructureFilter
import com.thoughtworks.archguard.insights.application.structure.StructureRepository
import org.archguard.domain.insight.InsightsParser
import org.springframework.stereotype.Service

@Service
class InsightApplicationService(
    val issueRepo: IssueInsightRepository,
    val scaRepo: ScaInsightRepository,
    val structRepo: StructureRepository
) {
    fun byExpression(id: Long?, expression: String, type: String): InsightModel {
        val query = InsightsParser.parse(expression)

        return when (type) {
            "sca" -> {
                val insightModelDtos = if (id == null || id == 0L) {
                    scaRepo.filterByCondition(query)
                } else {
                    scaRepo.filterByConditionWithSystemId(id, query)
                }

                val filtered = ScaInsightFilter.byInsight(query, insightModelDtos)
                InsightModel(filtered.size, filtered)
            }
            "issue" -> {
                val issueModelDtos = if (id == null || id == 0L) {
                    issueRepo.filterByCondition(query)
                } else {
                    issueRepo.filterByConditionWithSystemId(id, query)
                }

                val filtered = IssueInsightFilter.byInsight(query, issueModelDtos)
                InsightModel(filtered.size, filtered)
            }
            "class" -> {
                val structs = if (id == null || id == 0L) {
                    structRepo.filterByCondition(query)
                } else {
                    structRepo.filterByConditionWithSystemId(id, query)
                }

                val filtered = StructureFilter.byInsight(query, structs)
                InsightModel(filtered.size, filtered)
            }
            else -> {
                InsightModel(0, listOf())
            }
        }
    }
}

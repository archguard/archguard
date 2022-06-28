package com.thoughtworks.archguard.insights.application

import org.archguard.domain.insight.FieldFilter
import org.springframework.stereotype.Service

@Service
class InsightApplicationService(val repository: ScaInsightRepository) {
    fun byExpression(id: Long?, expression: String, type: String): InsightModel {
        val insightModelDtos: List<InsightModelDto>
        val models = FieldFilter.parse(expression)

        return when (type) {
            "sca" -> {
                insightModelDtos = if (id == null || id == 0L) {
                    repository.filterByCondition(models)
                } else {
                    repository.filterByConditionWithSystemId(id, models)
                }

                val filtered = ScaInsightFilter.filterByInsight(models, insightModelDtos)

                InsightModel(filtered.size, filtered)
            }

            else -> {
                InsightModel(0, listOf())
            }
        }
    }
}

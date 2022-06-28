package com.thoughtworks.archguard.insights.application

import org.archguard.domain.insight.FieldFilter
import org.springframework.stereotype.Service

@Service
class InsightApplicationService(val repository: ScaInsightRepository) {
    fun byExpression(id: Long?, expression: String, type: String): List<ScaModelDto> {
        val scaModelDtos: List<ScaModelDto>
        val models = FieldFilter.parse(expression)

        return when (type) {
            "sca" -> {
                scaModelDtos = if (id == null || id == 0L) {
                    repository.filterByCondition(models)
                } else {
                    repository.filterByConditionWithSystemId(id, models)
                }

                ScaInsightFilter.filterByInsight(models, scaModelDtos)
            }
            else -> {
                listOf()
            }
        }
    }
}

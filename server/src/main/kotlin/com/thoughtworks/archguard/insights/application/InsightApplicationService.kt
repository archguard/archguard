package com.thoughtworks.archguard.insights.application

import org.archguard.domain.insight.FieldFilter
import org.springframework.stereotype.Service

@Service
class InsightApplicationService(val repository: ScaInsightRepository) {
    fun byExpression(id: Long?, expression: String): List<ScaModelDto> {
        val scaModelDtos: List<ScaModelDto>
        val models = FieldFilter.parse(expression)

        if (id == null || id == 0L) {
            scaModelDtos = repository.filterByCondition(models)
        } else {
            scaModelDtos = repository.filterByConditionWithSystemId(id, models)
        }

        return ScaInsightFilter.filterByInsight(models, scaModelDtos);
    }
}

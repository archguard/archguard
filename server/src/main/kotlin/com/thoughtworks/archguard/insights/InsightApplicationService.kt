package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.ScaInsightFilter
import com.thoughtworks.archguard.insights.domain.ScaModelDto
import org.archguard.domain.insight.InsightFilter
import org.springframework.stereotype.Service

@Service
class InsightApplicationService(val repository: InsightRepository) {
    fun byExpression(id: Long?, expression: String): List<ScaModelDto> {
        val scaModelDtos: List<ScaModelDto>
        val models = InsightFilter.parse(expression)

        if (id == null || id == 0L) {
            scaModelDtos = repository.filterByCondition(models)
        } else {
            scaModelDtos = repository.filterByConditionWithSystemId(id, models)
        }

        return ScaInsightFilter.filterByInsight(models, scaModelDtos);
    }
}

package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.ScaInsightFilter
import com.thoughtworks.archguard.insights.domain.ScaModelDto
import org.archguard.domain.insight.InsightModel
import org.springframework.stereotype.Service

@Service
class InsightService(val repository: InsightRepository) {
    fun byScaArtifact(id: Long, expression: String): List<ScaModelDto> {
        val scaModelDtos = repository.filterByCondition(id)
        val models = InsightModel.parse(expression)
        return ScaInsightFilter.filterByInsight(models, scaModelDtos)
    }
}

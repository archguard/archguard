package com.thoughtworks.archguard.insights

import org.archguard.domain.insight.InsightModel
import org.archguard.domain.version.VersionComparison
import org.springframework.stereotype.Service

@Service
class InsightService(val repository: InsightRepository) {
    private var versionFilter: Pair<String, String>? = null

    fun byScaArtifact(id: Long, models: List<InsightModel>): List<ScaModelDto> {
        val comparison = VersionComparison()
        val scaModelDtos = repository.filterByCondition(id)

        models.map { insight ->
            when (insight.field) {
                "version" -> {
                    versionFilter = insight.valueExpr.value to insight.valueExpr.comparison
                }
                else -> {}
            }
        }

        if (versionFilter == null) {
            return scaModelDtos
        }

        return scaModelDtos.filter {
            comparison.eval(it.dep_version, versionFilter!!.second, versionFilter!!.first)
        }
    }
}

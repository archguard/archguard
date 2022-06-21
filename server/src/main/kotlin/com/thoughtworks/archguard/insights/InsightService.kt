package com.thoughtworks.archguard.insights

import org.archguard.domain.insight.InsightModel
import org.archguard.domain.version.VersionComparison
import org.archguard.domain.version.VersionNumber
import org.springframework.stereotype.Service

@Service
class InsightService(val repository: InsightRepository) {
    private var comparison: VersionComparison? = null

    fun byScaArtifact(id: Long, models: List<InsightModel>): List<ScaModelDto> {
        val scaModelDtos = repository.filterByCondition(id)

        models.map { insight ->
            when (insight.field) {
                "version" -> {
                    val versionNumber = VersionNumber.parse(insight.valueExpr.value)
                    if (versionNumber != null) {
                        comparison = VersionComparison(versionNumber, insight.valueExpr.comparison)
                    }
                }
                else -> {}
            }
        }

        if (comparison == null) {
            return scaModelDtos
        }

        return scaModelDtos.filter {
            comparison!!.isFit(it.dep_version)
        }
    }
}

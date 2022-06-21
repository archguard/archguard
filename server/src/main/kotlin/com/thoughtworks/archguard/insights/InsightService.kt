package com.thoughtworks.archguard.insights

import org.archguard.domain.insight.InsightModel
import org.archguard.domain.version.VersionComparison
import org.archguard.domain.version.VersionNumber
import org.springframework.stereotype.Service

@Service
class InsightService(val repository: InsightRepository) {
    private var versionComparison: VersionComparison? = null

    fun byScaArtifact(id: Long, models: List<InsightModel>): List<ScaModelDto> {
        val scaModelDtos = repository.filterByCondition(id)

        models.map { insight ->
            when (insight.field) {
                "version" -> {
                    val versionNumber = VersionNumber.parse(insight.valueExpr.value)
                    if (versionNumber != null) {
                        versionComparison = VersionComparison(versionNumber, insight.valueExpr.comparison)
                    }
                }
                else -> {}
            }
        }

        if (versionComparison == null) {
            return scaModelDtos
        }

        return scaModelDtos.filter {
            isSatisfied(it)
        }
    }

    // in here, the dep_version is in right, so it need to reverse
    private fun isSatisfied(it: ScaModelDto) = !versionComparison!!.isFit(it.dep_version)
}

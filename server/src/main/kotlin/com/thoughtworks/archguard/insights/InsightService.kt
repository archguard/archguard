package com.thoughtworks.archguard.insights

import org.archguard.domain.insight.InsightFieldFilter
import org.archguard.domain.insight.InsightModel
import org.archguard.domain.version.VersionComparison
import org.springframework.stereotype.Service

@Service
class InsightService(val repository: InsightRepository) {
    fun byScaArtifact(id: Long, models: List<InsightModel>): List<ScaModelDto> {
        var versionFilter: Pair<String, String>? = null
        val versionComparison = VersionComparison()
        var nameFilter: InsightFieldFilter? = null

        val scaModelDtos = repository.filterByCondition(id)

        models.map { insight ->
            when (insight.field) {
                "version" -> {
                    versionFilter = insight.valueExpr.value to insight.valueExpr.comparison
                }
                "name" -> {
                    nameFilter = InsightFieldFilter(insight.fieldFilter.type, insight.fieldFilter.value)
                }
                else -> {}
            }
        }

        if (versionFilter == null) {
            return scaModelDtos
        }

        return scaModelDtos.filter {
            val versionFilter = versionComparison.eval(it.dep_version, versionFilter!!.second, versionFilter!!.first)
            val nameValidate = nameFilter == null || nameFilter!!.validate(it.dep_name)

            versionFilter && nameValidate
        }
    }
}

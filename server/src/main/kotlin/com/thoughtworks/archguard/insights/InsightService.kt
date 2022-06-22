package com.thoughtworks.archguard.insights

import org.archguard.domain.insight.InsightFieldFilter
import org.archguard.domain.insight.InsightModel
import org.archguard.domain.version.VersionComparison
import org.springframework.stereotype.Service

@Service
class InsightService(val repository: InsightRepository) {
    fun byScaArtifact(id: Long, models: List<InsightModel>): List<ScaModelDto> {
        val scaModelDtos = repository.filterByCondition(id)

        val filteredModels = filterByInsight(models, scaModelDtos)

        return filteredModels
    }

    private fun filterByInsight(
        models: List<InsightModel>,
        scaModelDtos: List<ScaModelDto>,
    ): List<ScaModelDto> {
        val versionComparison = VersionComparison()
        var versionFilter: Pair<String, String>? = null
        var nameFilter: InsightFieldFilter? = null

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


        val filteredModels = scaModelDtos.filter {
            isVersionValid(versionComparison, versionFilter, it.dep_version) && isNameValid(nameFilter, it.dep_name)
        }

        return filteredModels
    }

    private fun isVersionValid(
        versionComparison: VersionComparison,
        versionFilter: Pair<String, String>?,
        leftVersion: String,
    ): Boolean {
        if (versionFilter == null) {
            return true
        }

        val comparison = versionFilter.second
        val insightVersion = versionFilter.first
        return versionComparison.eval(leftVersion, comparison, insightVersion)
    }

    private fun isNameValid(nameFilter: InsightFieldFilter?, name: String): Boolean {
        if (nameFilter == null) return true

        return nameFilter.validate(name)
    }
}

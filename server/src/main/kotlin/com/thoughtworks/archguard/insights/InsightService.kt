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
            isVersionValid(versionComparison, it, versionFilter) && isNameValid(nameFilter, it)
        }
        return filteredModels
    }

    private fun isVersionValid(
        versionComparison: VersionComparison,
        it: ScaModelDto,
        versionFilter: Pair<String, String>?,
    ): Boolean {
        if (versionFilter == null) {
            return true
        }

        return versionComparison.eval(it.dep_version, versionFilter.second, versionFilter.first)
    }

    private fun isNameValid(
        nameFilter: InsightFieldFilter?,
        it: ScaModelDto,
    ): Boolean {
        if (nameFilter == null) return true

        return nameFilter.validate(it.dep_name)
    }
}

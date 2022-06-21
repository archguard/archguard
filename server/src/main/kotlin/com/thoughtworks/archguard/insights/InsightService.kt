package com.thoughtworks.archguard.insights

import org.archguard.domain.insight.InsightModel
import org.springframework.stereotype.Service

@Service
class InsightService(val repository: InsightRepository) {
    fun byScaArtifact(id: Long, models: List<InsightModel>): List<ScaModelDto> {
        val mappings = toDbColumns(models)

        return repository.filterByCondition(id, mappings)
    }

    private fun toDbColumns(models: List<InsightModel>): MutableMap<String, String> {
        val mappings: MutableMap<String, String> = mutableMapOf()

        models.map {
            val field = when (it.field) {
                "group" -> "dep_group"
                "artifact" -> "dep_artifact"
                "version" -> "dep_version"
                else -> it.field
            }

            mappings[field] = it.field
        }

        return mappings
    }
}

package com.thoughtworks.archguard.insights.application.structure

import com.thoughtworks.archguard.insights.application.StructureModelDto
import com.thoughtworks.archguard.insights.application.postFilter
import org.archguard.domain.insight.Query
import org.archguard.domain.insight.RegexQuery

class StructureFilter {
    companion object {
        fun byInsight(query: Query, models: List<StructureModelDto>): List<StructureModelDto> {
            return postFilter(query, models, ::filterModel)
        }

        private fun filterModel(data: StructureModelDto, condition: RegexQuery): Boolean {
            return when (condition.field) {
                "name" -> data.name.matches(condition.regex)
                "package_name" -> data.package_name.matches(condition.regex)
                else -> {
                    false
                }
            }
        }
    }
}
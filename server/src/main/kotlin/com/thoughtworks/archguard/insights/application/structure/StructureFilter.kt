package com.thoughtworks.archguard.insights.application.structure

import com.thoughtworks.archguard.insights.application.StructureModelDto
import com.thoughtworks.archguard.insights.application.postFilter
import org.archguard.domain.insight.Either
import org.archguard.domain.insight.Query
import org.archguard.domain.insight.RegexQuery
import org.archguard.domain.insight.VersionQuery
import org.archguard.domain.insight.support.InsightIllegalException
import org.archguard.domain.version.VersionComparison
import org.archguard.domain.version.VersionNumber

class StructureFilter {
    companion object {
        fun byInsight(query: Query, models: List<StructureModelDto>): List<StructureModelDto> {
            return postFilter(query, models, ::filterModel)
        }

        private fun filterModel(data: StructureModelDto, condition: Either<RegexQuery, VersionQuery>) =
            if (condition.isLeft) {
                val regexCondition = condition.getLeftOrNull()!!
                when (regexCondition.field) {
                    "name" -> data.name.matches(regexCondition.regex)
                    "package_name" -> data.package_name.matches(regexCondition.regex)
                    else -> {
                        false
                    }
                }
            } else {
                val versionCondition = condition.getRightOrNull()!!
                when (versionCondition.filed) {
                    "name" -> VersionComparison(
                        VersionNumber.parse(data.name) ?: throw InsightIllegalException("Version is not valid"),
                        versionCondition.operator
                    ).toOther(versionCondition.version)
                    "package_name" -> VersionComparison(
                        VersionNumber.parse(data.package_name) ?: throw InsightIllegalException("Version is not valid"),
                        versionCondition.operator
                    ).toOther(versionCondition.version)

                    else -> false
                }
            }
    }

}
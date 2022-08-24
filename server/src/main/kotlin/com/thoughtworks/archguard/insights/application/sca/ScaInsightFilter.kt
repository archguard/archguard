package com.thoughtworks.archguard.insights.application.sca

import com.thoughtworks.archguard.insights.application.InsightModelDto
import com.thoughtworks.archguard.insights.application.postFilter
import org.archguard.domain.insight.Either
import org.archguard.domain.insight.Query
import org.archguard.domain.insight.RegexQuery
import org.archguard.domain.insight.VersionQuery
import org.archguard.domain.insight.support.InsightIllegalException
import org.archguard.domain.version.VersionComparison
import org.archguard.domain.version.VersionNumber

object ScaInsightFilter {
    fun byInsight(query: Query, models: List<InsightModelDto>): List<InsightModelDto> {
        return postFilter(query, models, ::filterModel)
    }

    private fun filterModel(
        data: InsightModelDto,
        condition: Either<RegexQuery, VersionQuery>
    ) = if (condition.isLeft) {
        val regexCondition = condition.getLeftOrNull()!!
        when (regexCondition.field) {
            "dep_name" -> data.dep_name.matches(regexCondition.regex)
            "dep_version" -> data.dep_version.matches(regexCondition.regex)
            "dep_artifact" -> data.dep_artifact.matches(regexCondition.regex)
            "dep_group" -> data.dep_group.matches(regexCondition.regex)
            else -> true
        }
    } else {
        val versionCondition = condition.getRightOrNull()!!
        when (versionCondition.filed) {
            "dep_name" -> VersionComparison(
                VersionNumber.parse(data.dep_name) ?: throw InsightIllegalException("Version is not valid"),
                versionCondition.operator
            ).toOther(versionCondition.version)

            "dep_version" -> VersionComparison(
                VersionNumber.parse(data.dep_version) ?: throw InsightIllegalException("Version is not valid"),
                versionCondition.operator
            ).toOther(versionCondition.version)

            "dep_artifact" -> VersionComparison(
                VersionNumber.parse(data.dep_artifact) ?: throw InsightIllegalException("Version is not valid"),
                versionCondition.operator
            ).toOther(versionCondition.version)

            "dep_group" -> VersionComparison(
                VersionNumber.parse(data.dep_group) ?: throw InsightIllegalException("Version is not valid"),
                versionCondition.operator
            ).toOther(versionCondition.version)

            else -> true
        }
    }
}


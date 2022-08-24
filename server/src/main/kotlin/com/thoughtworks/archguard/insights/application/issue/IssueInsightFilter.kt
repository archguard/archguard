package com.thoughtworks.archguard.insights.application.issue

import com.thoughtworks.archguard.insights.application.IssueModelDto
import com.thoughtworks.archguard.insights.application.postFilter
import org.archguard.domain.insight.Either
import org.archguard.domain.insight.Query
import org.archguard.domain.insight.RegexQuery
import org.archguard.domain.insight.VersionQuery
import org.archguard.domain.insight.support.InsightIllegalException
import org.archguard.domain.version.VersionComparison
import org.archguard.domain.version.VersionNumber

object IssueInsightFilter {
    fun byInsight(query: Query, models: List<IssueModelDto>): List<IssueModelDto> {
        return postFilter(query, models, ::filterModel)
    }

    private fun filterModel(
        data: IssueModelDto,
        condition: Either<RegexQuery, VersionQuery>
    ) = if (condition.isLeft) {
        val regexCondition = condition.getLeftOrNull()!!
        when (regexCondition.field) {
            "name" -> data.name.matches(regexCondition.regex)
            "severity" -> data.severity.matches(regexCondition.regex)
            "rule_id" -> data.rule_id.matches(regexCondition.regex)
            "rule_type" -> data.rule_type.matches(regexCondition.regex)
            else -> true
        }
    } else {
        val versionCondition = condition.getRightOrNull()!!

        when (versionCondition.filed) {
            "name" -> VersionComparison(
                VersionNumber.parse(data.name) ?: throw InsightIllegalException("Version is not valid"),
                versionCondition.operator
            ).toOther(versionCondition.version)

            "severity" -> VersionComparison(
                VersionNumber.parse(data.severity) ?: throw InsightIllegalException("Version is not valid"),
                versionCondition.operator
            ).toOther(versionCondition.version)

            "rule_id" -> VersionComparison(
                VersionNumber.parse(data.rule_id) ?: throw InsightIllegalException("Version is not valid"),
                versionCondition.operator
            ).toOther(versionCondition.version)

            "rule_type" -> VersionComparison(
                VersionNumber.parse(data.rule_type) ?: throw InsightIllegalException("Version is not valid"),
                versionCondition.operator
            ).toOther(versionCondition.version)

            else -> true
        }
    }
}


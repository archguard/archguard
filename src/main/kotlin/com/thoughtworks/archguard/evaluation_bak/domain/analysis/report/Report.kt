package com.thoughtworks.archguard.evaluation_bak.domain.analysis.report

import java.util.*

interface Report {
    fun getImprovements(): List<String>
    fun getLevel(): Map<ReportDms, ReportLevel>
    fun getComment(): ReportLevel {
        val needCount = getLevel().filterValues { it == ReportLevel.NEED_IMPROVED }.count()
        val goodCount = getLevel().filterValues { it == ReportLevel.GOOD }.count()
        val wellCount = getLevel().filterValues { it == ReportLevel.WELL }.count()
        when (Collections.max(listOf(needCount, goodCount, wellCount))) {
            needCount -> {
                return ReportLevel.NEED_IMPROVED
            }
            wellCount -> {
                return ReportLevel.WELL;
            }
            goodCount -> {
                return ReportLevel.GOOD;
            }
        }
        return ReportLevel.NEED_IMPROVED
    }

    fun getReportDetail(): ReportDetail
}

interface ReportDetail {

}

enum class ReportDms {
    LatestModuleTestCoverage,
    LatestTestCoverage,
    UselessTestPercent,
    LatestModuleInstability,
    ScatteredCommit
}

enum class ReportLevel {
    GOOD,
    WELL,
    NEED_IMPROVED

}

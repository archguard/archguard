package com.thoughtworks.archguard.evaluation.domain.analysis.report

interface Report {
    fun getImprovements(): List<String>
    fun getLevel(): Map<ReportDms, ReportLevel>
    fun getComment(): ReportLevel
}

enum class ReportDms {
    LatestModuleTestCoverage,
    LatestTestCoverage,
    UselessTestPercent
}

enum class ReportLevel {
    GOOD,
    WELL,
    NEED_IMPROVED

}

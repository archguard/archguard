package com.thoughtworks.archguard.evaluation.domain.analysis.report

interface Report {
    fun getImprovements(): List<String>
    fun getLevel(): Map<ReportDms, ReportLevel>
    fun getComment(): ReportLevel
}

interface ReportDms {
    fun getDms(): String
}

enum class ReportLevel {
    GOOD,
    WELL,
    NEED_IMPROVED

}

package com.thoughtworks.archguard.evaluation.domain.analysis.report

interface Report {
    fun getImprovements(): List<String>
    fun getLevel(): Map<String, ReportLevel>
}

enum class ReportLevel {
    GOOD,
    WELL,
    NEED_IMPROVED

}

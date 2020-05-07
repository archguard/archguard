package com.thoughtworks.archguard.evaluation.domain.analysis.report

import com.thoughtworks.archguard.report.domain.model.CommitLog

class ChangeImpactQualityReport(val scatteredCommits: List<CommitLog>) : Report {
    override fun getImprovements(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getLevel(): Map<ReportDms, ReportLevel> {
        TODO("Not yet implemented")
    }
}
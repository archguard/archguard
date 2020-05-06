package com.thoughtworks.archguard.evaluation.domain.analysis.report

data class ModuleCouplingQualityReport(val moduleInstability: Double,
                                       val moduleFanIn: Int,
                                       val moduleFanOut: Int) : Report {
    override fun getImprovements(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getLevel(): Map<ReportDms, ReportLevel> {
        TODO("Not yet implemented")
    }

    override fun getComment(): ReportLevel {
        TODO("Not yet implemented")
    }
}
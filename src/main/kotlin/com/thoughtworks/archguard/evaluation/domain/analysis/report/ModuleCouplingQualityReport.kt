package com.thoughtworks.archguard.evaluation.domain.analysis.report

data class ModuleCouplingQualityReport(val latestQualityList: List<ModuleCouplingQuality>) : Report {

    override fun getImprovements(): List<String> {
        return getLevel().filterValues { it == ReportLevel.NEED_IMPROVED }
                .keys.map {
                    when (it) {
                        ReportDms.LatestModuleInstability -> {
                            String.format("核心模块平均不稳定性为%f%%，核心模块稳定性差，会导致核心模块脆弱，容易被破坏。", getAverage() * 100)
                        }
                        else -> ""
                    }
                }
    }

    private fun getAverage(): Double {
       return latestQualityList.map { it.moduleInstability }.average()
    }

    override fun getLevel(): Map<ReportDms, ReportLevel> {
        val count8 = latestQualityList.filter { it.moduleInstability > 0.8 }.size
        val count8To6 = latestQualityList.filter { it.moduleInstability < 0.8 && it.moduleInstability > 0.6 }.size
        val count6 = latestQualityList.filter { it.moduleInstability < 0.6 }.size
        val max = listOf(count8, count8To6, count6).max()
        val result = HashMap<ReportDms, ReportLevel>()
        when (max) {
            count8 -> result[ReportDms.LatestModuleInstability] = ReportLevel.GOOD
            count8To6 -> result[ReportDms.LatestModuleInstability] = ReportLevel.WELL
            count6 -> result[ReportDms.LatestModuleInstability] = ReportLevel.NEED_IMPROVED
        }
        return result
    }
}

data class ModuleCouplingQuality(val packageName: String,
                                 val moduleInstability: Double,
                                 val moduleFanIn: Int,
                                 val moduleFanOut: Int)
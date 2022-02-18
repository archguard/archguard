package com.thoughtworks.archguard.evaluation_bak.domain.analysis.report

import com.thoughtworks.archguard.evaluation_bak.domain.ModuleCouplingQuality
import com.thoughtworks.archguard.evaluation_bak.domain.ModuleCouplingReportDetail

class ModuleCouplingQualityReport(latestQualityList: List<ModuleCouplingQuality>) : Report {

    private val detail: ModuleCouplingReportDetail = ModuleCouplingReportDetail(latestQualityList)

    override fun getImprovements(): List<String> {
        return getLevel().filterValues { it == ReportLevel.NEED_IMPROVED }
                .keys.map {
                    when (it) {
                        ReportDms.LatestModuleInstability -> {
                            String.format("核心模块平均不稳定性为%f%%，核心模块稳定性差，会导致核心模块脆弱，容易被破坏。", detail.moduleInstabilityAverage * 100)
                        }
                        else -> ""
                    }
                }
    }

    override fun getLevel(): Map<ReportDms, ReportLevel> {
        val max = listOf(detail.count8,
                detail.count8To6,
                detail.count6)
                .max()
        val result = HashMap<ReportDms, ReportLevel>()
        when (max) {
            detail.count8 -> result[ReportDms.LatestModuleInstability] = ReportLevel.GOOD
            detail.count8To6 -> result[ReportDms.LatestModuleInstability] = ReportLevel.WELL
            detail.count6 -> result[ReportDms.LatestModuleInstability] = ReportLevel.NEED_IMPROVED
        }
        return result
    }

    override fun getReportDetail(): ReportDetail {
        return detail
    }
}
package com.thoughtworks.archguard.evaluation.domain.analysis.report

class ChangeImpactQualityReport(val scatteredPercent: Double) : Report {
    override fun getImprovements(): List<String> {
        return getLevel().filterValues { it == ReportLevel.NEED_IMPROVED }
                .keys.map {
                    when (it) {
                        ReportDms.LatestModuleInstability -> {
                            String.format("最近三个月提交的中有%f%%的散弹式提交，代码内聚性差，可能存在未识别的领域模型。", scatteredPercent * 100)
                        }
                        else -> ""
                    }
                }
    }

    override fun getLevel(): Map<ReportDms, ReportLevel> {
        val result = HashMap<ReportDms, ReportLevel>()
        when {
            scatteredPercent > 0.1 -> result[ReportDms.LatestModuleInstability] = ReportLevel.NEED_IMPROVED
            scatteredPercent > 0.05 -> result[ReportDms.LatestModuleInstability] = ReportLevel.WELL
            scatteredPercent < 0.05 -> result[ReportDms.LatestModuleInstability] = ReportLevel.GOOD
        }
        return result
    }
}
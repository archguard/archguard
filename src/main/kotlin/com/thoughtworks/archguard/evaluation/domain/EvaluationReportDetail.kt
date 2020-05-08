package com.thoughtworks.archguard.evaluation.domain

import com.thoughtworks.archguard.evaluation.domain.analysis.report.ReportDetail
import com.thoughtworks.archguard.report.domain.model.CommitLog

data class EvaluationReportDetail(var changeImpactReportDetail: ChangeImpactReportDetail?,
                                  var codeStyleReportDetail: CodeStyleReportDetail?,
                                  var dbCouplingReportDetail: DbCouplingReportDetail?,
                                  var layerReportDetail: LayerReportDetail?,
                                  var moduleCouplingReportDetail: ModuleCouplingReportDetail?,
                                  var testProtectionReportDetail: TestProtectionReportDetail?) {

    constructor(changeImpactReportDetail: List<ReportDetail>) : this(null, null, null, null, null, null) {
        for (detail: ReportDetail in changeImpactReportDetail) {
            when (detail) {
                is ChangeImpactReportDetail -> this.changeImpactReportDetail = detail
                is CodeStyleReportDetail -> this.codeStyleReportDetail = detail
                is DbCouplingReportDetail -> this.dbCouplingReportDetail = detail
                is LayerReportDetail -> this.layerReportDetail = detail
                is ModuleCouplingReportDetail -> this.moduleCouplingReportDetail = detail
                is TestProtectionReportDetail -> this.testProtectionReportDetail = detail
            }
        }
    }

}

data class TestProtectionReportDetail(val uselessPercent: Double, val latestUselessTest: Int, val latestTestCoverage: Double, val latestModuleTestCoverage: Double) : ReportDetail {
    val testCoverage = latestTestCoverage - latestUselessTest * 0.01
    val modelCoverage = latestModuleTestCoverage - latestUselessTest * 0.01
}

data class ModuleCouplingReportDetail(val latestQualityList: List<ModuleCouplingQuality>) : ReportDetail {
    val moduleInstabilityAverage = latestQualityList.map { it.moduleInstability }.average()
    val count8 = latestQualityList.filter { it.moduleInstability > 0.8 }.size
    val count8To6 = latestQualityList.filter { it.moduleInstability < 0.8 && it.moduleInstability > 0.6 }.size
    val count6 = latestQualityList.filter { it.moduleInstability < 0.6 }.size

}

data class ModuleCouplingQuality(val packageName: String,
                                 val moduleInstability: Double,
                                 val moduleFanIn: Int,
                                 val moduleFanOut: Int)

class LayerReportDetail : ReportDetail {

}

class DbCouplingReportDetail : ReportDetail {

}

class CodeStyleReportDetail : ReportDetail {

}

data class ChangeImpactReportDetail(val scatteredCommits: List<CommitLog>, val allCommits: List<CommitLog>) : ReportDetail {
    val scatteredPercent = scatteredCommits.size.toDouble() / allCommits.size
}

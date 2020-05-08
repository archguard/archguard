package com.thoughtworks.archguard.evaluation.domain

import com.thoughtworks.archguard.evaluation.domain.analysis.report.ReportDetail
import com.thoughtworks.archguard.report.domain.model.CommitLog

data class EvaluationReportDetail(val changeImpactReportDetail: ChangeImpactReportDetail,
                                  val codeStyleReportDetail: CodeStyleReportDetail,
                                  val dbCouplingReportDetail: DbCouplingReportDetail,
                                  val layerReportDetail: LayerReportDetail,
                                  val moduleCouplingReportDetail: ModuleCouplingReportDetail,
                                  val testProtectionReportDetail: TestProtectionReportDetail) {


}

class TestProtectionReportDetail : ReportDetail {

}

class ModuleCouplingReportDetail : ReportDetail {

}

class LayerReportDetail : ReportDetail {

}

class DbCouplingReportDetail : ReportDetail {

}

class CodeStyleReportDetail : ReportDetail {

}

data class ChangeImpactReportDetail(val scatteredCommits: List<CommitLog>, val allCommits: List<CommitLog>) : ReportDetail {
    val scatteredPercent = scatteredCommits.size.toDouble() / allCommits.size
}

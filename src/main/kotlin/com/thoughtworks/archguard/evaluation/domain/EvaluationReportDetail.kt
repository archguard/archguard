package com.thoughtworks.archguard.evaluation.domain

import com.thoughtworks.archguard.evaluation.domain.analysis.report.ReportDetail
import com.thoughtworks.archguard.evaluation.infrastructure.enumContains
import com.thoughtworks.archguard.report.domain.model.CommitLog
import com.thoughtworks.archguard.report.infrastructure.TestBadSmellCountDBO

data class EvaluationReportDetail(var changeImpactReportDetail: ChangeImpactReportDetail? = null,
                                  var codeStyleReportDetail: CodeStyleReportDetail? = null,
                                  var dbCouplingReportDetail: DbCouplingReportDetail? = null,
                                  var layerReportDetail: LayerReportDetail? = null,
                                  var moduleCouplingReportDetail: ModuleCouplingReportDetail? = null,
                                  var testProtectionReportDetail: TestProtectionReportDetail? = null) {

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

data class TestProtectionReportDetail(val testBs: List<TestBadSmellCountDBO>? = null, val totalTest: Int? = null,
                                      val hotSpotTest: List<String>? = null,
                                      val hotSpotTestBadSmell: List<TestBadSmellCountDBO>? = null,
                                      val classCoverageByFiles: List<Pair<Double, Double>>? = null,
                                      val hotSpotFile: List<String>? = null,
                                      val classCoverageByModules: List<Pair<Double, Double>>? = null,
                                      val hotSpotModule: List<String>? = null) : ReportDetail {
    val uselessTest = (testBs?.filter { enumContains<TestBadSmellType>(it.type) }?.sumBy { it.size }) ?: 0
    val latestUselessTest = (hotSpotTestBadSmell?.filter { enumContains<TestBadSmellType>(it.type) }?.sumBy { it.size })
            ?: 0
    val uselessPercent = generateUselessPercent()
    val latestTestCoverage = (classCoverageByFiles?.map {
        if (it.second < 1) {
            0.0
        } else {
            it.first.div(it.second)
        }
    }?.average()?.times(classCoverageByFiles.size)?.div((hotSpotFile?.size) ?: 1)) ?: 0.0

    val latestModuleTestCoverage = (classCoverageByModules?.map {
        if (it.second < 1) {
            0.0
        } else {
            it.first.div(it.second)
        }
    }?.average()) ?: 0.0
    val testCoverage = latestTestCoverage.minus(latestUselessTest.times(0.01))
    val modelCoverage = latestModuleTestCoverage.minus(latestUselessTest.times(0.01))

    private fun generateUselessPercent(): Double {
        if (uselessTest == 0) {
            return 0.0
        }
        return uselessTest.toDouble().div(totalTest ?: 1)
    }

}

data class ModuleCouplingReportDetail(val latestQualityList: List<ModuleCouplingQuality>? = null) : ReportDetail {
    val moduleInstabilityAverage = (latestQualityList?.map { it.moduleInstability }?.average()) ?: 0.0
    val count8 = (latestQualityList?.filter { it.moduleInstability > 0.8 }?.size) ?: 0
    val count8To6 = (latestQualityList?.filter { it.moduleInstability < 0.8 && it.moduleInstability > 0.6 }?.size) ?: 0
    val count6 = (latestQualityList?.filter { it.moduleInstability < 0.6 }?.size) ?: 0

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

data class ChangeImpactReportDetail(val scatteredCommits: List<CommitLog>? = null,
                                    val allCommits: List<CommitLog>? = null) : ReportDetail {
    val scatteredPercent = (scatteredCommits?.size?.toDouble() ?: 0.0).div(allCommits?.size ?: 1)
}

enum class TestBadSmellType {
    IgnoreTest,
    EmptyTest,
    RedundantAssertionTest,
    UnknownTest;
}
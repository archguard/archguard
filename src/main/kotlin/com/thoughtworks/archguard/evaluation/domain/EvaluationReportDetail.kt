package com.thoughtworks.archguard.evaluation.domain

import com.thoughtworks.archguard.evaluation.domain.analysis.report.ReportDetail
import com.thoughtworks.archguard.evaluation.infrastructure.enumContains
import com.thoughtworks.archguard.report.domain.model.CommitLog
import com.thoughtworks.archguard.report.infrastructure.TestBadSmellCountDBO

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

data class TestProtectionReportDetail(val testBs: List<TestBadSmellCountDBO>, val totalTest: Int,
                                      val hotSpotTest: List<String>,
                                      val hotSpotTestBadSmell: List<TestBadSmellCountDBO>,
                                      val classCoverageByFiles: List<Pair<Double, Double>>,
                                      val hotSpotFile: List<String>,
                                      val classCoverageByModules: List<Pair<Double, Double>>,
                                      val hotSpotModule: List<String>) : ReportDetail {
    val uselessTest = testBs.filter { enumContains<TestBadSmellType>(it.type) }.sumBy { it.size }
    val latestUselessTest = hotSpotTestBadSmell.filter { enumContains<TestBadSmellType>(it.type) }.sumBy { it.size }
    val uselessPercent = generateUselessPercent()
    val latestTestCoverage = classCoverageByFiles.map {
        if (it.second < 1) {
            0.0
        } else {
            it.first / it.second
        }
    }.average() * classCoverageByFiles.size / hotSpotFile.size

    val latestModuleTestCoverage = classCoverageByModules.map {
        if (it.second < 1) {
            0.0
        } else {
            it.first / it.second
        }
    }.average()
    val testCoverage = latestTestCoverage - latestUselessTest * 0.01
    val modelCoverage = latestModuleTestCoverage - latestUselessTest * 0.01

    private fun generateUselessPercent(): Double {
        if (uselessTest == 0) {
            return 0.0
        }
        return uselessTest.toDouble() / totalTest
    }

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

enum class TestBadSmellType {
    IgnoreTest,
    EmptyTest,
    RedundantAssertionTest,
    UnknownTest;
}
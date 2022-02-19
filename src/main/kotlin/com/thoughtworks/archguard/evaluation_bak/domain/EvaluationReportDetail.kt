package com.thoughtworks.archguard.evaluation_bak.domain

import com.thoughtworks.archguard.evaluation_bak.domain.analysis.report.ReportDetail
import com.thoughtworks.archguard.evaluation_bak.infrastructure.enumContains
import com.thoughtworks.archguard.report_bak.domain.model.Bundle
import com.thoughtworks.archguard.report_bak.domain.model.CommitLog

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

data class TestProtectionReportDetail(val testBs: List<TestBadSmellCount>? = null, val totalTest: Int? = null,
                                      val hotSpotTest: List<String>? = null,
                                      val hotSpotTestBadSmell: List<TestBadSmellCount>? = null,
                                      val classCoverageByFiles: List<Bundle>? = null,
                                      val hotSpotFile: List<String>? = null,
                                      val classCoverageByModules: List<Bundle>? = null,
                                      val hotSpotModule: List<String>? = null) : ReportDetail {
    val uselessTest = getUselessTest(testBs)
    val latestUselessTest = getUselessTest(hotSpotTestBadSmell)
    val uselessPercent = generateUselessPercent()
    val latestTestCoverage = (classCoverageByFiles?.map { it.calculateClassCoveredPercent() }?.average()?.times(classCoverageByFiles.size)?.div((hotSpotFile?.size)
            ?: 1)) ?: 0.0
    val latestModuleTestCoverage = (classCoverageByModules?.map { it.calculateClassCoveredPercent() }?.average()) ?: 0.0
    val testCoverage = latestTestCoverage.minus(latestUselessTest.times(0.01))
    val modelCoverage = latestModuleTestCoverage.minus(latestUselessTest.times(0.01))

    private fun getUselessTest(testBadSmell: List<TestBadSmellCount>?): Int {
        return (testBadSmell?.filter {
            enumContains<TestBadSmellType>(it.type)
        }?.sumBy { it.size }) ?: 0
    }

    private fun generateUselessPercent(): Double {
        if (uselessTest == 0) {
            return 0.0
        }
        return uselessTest.toDouble().div(totalTest ?: 1)
    }
}

class TestBadSmellCount(val type: String, val size: Int) {
    constructor() : this("", 0)
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
                                 val moduleFanOut: Int) {
    constructor() : this("", 0.0, 0, 0)
}

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
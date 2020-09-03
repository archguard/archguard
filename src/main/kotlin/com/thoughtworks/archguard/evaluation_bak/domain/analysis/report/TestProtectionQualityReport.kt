package com.thoughtworks.archguard.evaluation_bak.domain.analysis.report

import com.thoughtworks.archguard.evaluation_bak.domain.TestBadSmellCount
import com.thoughtworks.archguard.evaluation_bak.domain.TestProtectionReportDetail
import com.thoughtworks.archguard.report_bak.domain.model.Bundle

class TestProtectionQualityReport(testBs: List<TestBadSmellCount>,
                                  totalTest: Int,
                                  hotSpotTest: List<String>,
                                  hotSpotTestBadSmell: List<TestBadSmellCount>,
                                  classCoverageByFiles: List<Bundle>,
                                  hotSpotFile: List<String>,
                                  classCoverageByModules: List<Bundle>,
                                  hotSpotModule: List<String>) : Report {
    private val detail: TestProtectionReportDetail = TestProtectionReportDetail(testBs, totalTest,
            hotSpotTest, hotSpotTestBadSmell,
            classCoverageByFiles, hotSpotFile,
            classCoverageByModules, hotSpotModule)

    override fun getImprovements(): List<String> {
        return getLevel().filterValues { it == ReportLevel.NEED_IMPROVED }
                .keys.map {
                    when (it) {
                        ReportDms.LatestModuleTestCoverage -> {
                            String.format("核心模块测试覆盖率是有%f%%，且存在%d个无效测试，对于核心模块，自动化测试不足，可能出现核心功能业务Bug。", detail.latestModuleTestCoverage * 100, detail.latestUselessTest)
                        }
                        ReportDms.LatestTestCoverage -> {
                            String.format("核心模块测试覆盖率是有%f%%，且存在%d个无效测试，对于最近新增的功能，自动化测试不足，可能需要更多的手动测试来覆盖。", detail.latestTestCoverage * 100, detail.latestUselessTest)
                        }
                        ReportDms.UselessTestPercent -> {
                            String.format("系统存无效测试占比%f%%，这些测试不能有效显示功能是否遭到破坏，易误导测试人员，出现少测，漏侧现象。", detail.uselessPercent * 100)
                        }
                        else -> ""
                    }
                }
    }

    override fun getLevel(): Map<ReportDms, ReportLevel> {
        val result = HashMap<ReportDms, ReportLevel>()
        when {
            detail.uselessPercent > 0.05 -> {
                result[ReportDms.UselessTestPercent] = ReportLevel.NEED_IMPROVED
            }
            detail.uselessPercent > 0.03 -> {
                result[ReportDms.UselessTestPercent] = ReportLevel.WELL
            }
            detail.uselessPercent < 0.03 -> {
                result[ReportDms.UselessTestPercent] = ReportLevel.GOOD
            }
        }

        when {
            detail.testCoverage > 0.8 -> {
                result[ReportDms.LatestTestCoverage] = ReportLevel.GOOD
            }
            detail.testCoverage > 0.6 -> {
                result[ReportDms.LatestTestCoverage] = ReportLevel.WELL
            }
            detail.testCoverage < 0.6 -> {
                result[ReportDms.LatestTestCoverage] = ReportLevel.NEED_IMPROVED
            }
        }

        when {
            detail.modelCoverage > 0.8 -> {
                result[ReportDms.LatestModuleTestCoverage] = ReportLevel.GOOD
            }
            detail.modelCoverage > 0.6 -> {
                result[ReportDms.LatestModuleTestCoverage] = ReportLevel.WELL
            }
            detail.modelCoverage < 0.6 -> {
                result[ReportDms.LatestModuleTestCoverage] = ReportLevel.NEED_IMPROVED
            }
        }
        return result
    }

    override fun getReportDetail(): ReportDetail {
        return detail
    }
}
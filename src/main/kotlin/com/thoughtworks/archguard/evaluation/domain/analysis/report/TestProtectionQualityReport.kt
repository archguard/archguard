package com.thoughtworks.archguard.evaluation.domain.analysis.report

import java.util.*
import kotlin.collections.HashMap

data class TestProtectionQualityReport(val uselessPercent: Double,
                                       val latestUselessTest: Int,
                                       val latestTestCoverage: Double,
                                       val latestModuleTestCoverage: Double) : Report {
    override fun getImprovements(): List<String> {
        return getLevel().filterValues { it == ReportLevel.NEED_IMPROVED }
                .keys.map {
                    when (it) {
                        ReportDms.LatestModuleTestCoverage -> {
                            String.format("核心模块测试覆盖率是有%f，且存在%d个无效测试，对于核心模块，自动化测试不足，可能出现核心功能业务Bug。", latestModuleTestCoverage, latestUselessTest)
                        }
                        ReportDms.LatestTestCoverage -> {
                            String.format("核心模块测试覆盖率是有%f，且存在%d个无效测试，对于最近新增的功能，自动化测试不足，可能需要更多的手动测试来覆盖。", latestTestCoverage, latestUselessTest)
                        }
                        ReportDms.UselessTestPercent -> {
                            String.format("系统存无效测试占比%f，这些测试不能有效显示功能是否遭到破坏，易误导测试人员，出现少测，漏侧现象。", uselessPercent)
                        }
                    }
                }
    }

    override fun getLevel(): Map<ReportDms, ReportLevel> {
        val result = HashMap<ReportDms, ReportLevel>()
        when {
            uselessPercent > 0.05 -> {
                result[ReportDms.UselessTestPercent] = ReportLevel.NEED_IMPROVED
            }
            uselessPercent > 0.03 -> {
                result[ReportDms.UselessTestPercent] = ReportLevel.WELL
            }
            uselessPercent < 0.03 -> {
                result[ReportDms.UselessTestPercent] = ReportLevel.GOOD
            }
        }

        val testCoverage = latestTestCoverage - latestUselessTest * 0.01
        when {
            testCoverage > 0.8 -> {
                result[ReportDms.LatestTestCoverage] = ReportLevel.GOOD
            }
            testCoverage > 0.6 -> {
                result[ReportDms.LatestTestCoverage] = ReportLevel.WELL
            }
            testCoverage < 0.6 -> {
                result[ReportDms.LatestTestCoverage] = ReportLevel.NEED_IMPROVED
            }
        }

        val coverage = latestModuleTestCoverage - latestUselessTest * 0.01
        when {
            coverage > 0.8 -> {
                result[ReportDms.LatestModuleTestCoverage] = ReportLevel.GOOD
            }
            uselessPercent > 0.6 -> {
                result[ReportDms.LatestModuleTestCoverage] = ReportLevel.WELL
            }
            uselessPercent < 0.6 -> {
                result[ReportDms.LatestModuleTestCoverage] = ReportLevel.NEED_IMPROVED
            }
        }
        return result
    }

    override fun getComment(): ReportLevel {
        val needCount = getLevel().filterValues { it == ReportLevel.NEED_IMPROVED }.count()
        val goodCount = getLevel().filterValues { it == ReportLevel.GOOD }.count()
        val wellCount = getLevel().filterValues { it == ReportLevel.WELL }.count()
        when (Collections.max(listOf(needCount, goodCount, wellCount))) {
            needCount -> {
                return ReportLevel.NEED_IMPROVED
            }
            wellCount -> {
                return ReportLevel.WELL;
            }
            goodCount -> {
                return ReportLevel.GOOD;
            }
        }
        return ReportLevel.NEED_IMPROVED
    }
}
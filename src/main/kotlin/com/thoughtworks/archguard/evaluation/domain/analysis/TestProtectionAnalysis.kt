package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import com.thoughtworks.archguard.evaluation.domain.analysis.report.ReportDms
import com.thoughtworks.archguard.evaluation.domain.analysis.report.ReportLevel
import com.thoughtworks.archguard.report.infrastructure.CoverageRepo
import com.thoughtworks.archguard.report.infrastructure.HotSpotRepo
import com.thoughtworks.archguard.report.infrastructure.TestBadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Collections.max

@Service
class TestProtectionAnalysis(@Autowired val testBadSmellRepo: TestBadSmellRepo,
                             @Autowired val coverageRepo: CoverageRepo,
                             @Autowired val hotSpotRepo: HotSpotRepo) : Analysis {
    override fun getName(): String {
        return "测试保护"
    }

    override fun getQualityReport(): Report {
        return TestProtectionQualityReport(calculateUselessPercent(),
                calculateLatestUselessTest(),
                calculateLatestTestCoverage(),
                calculateLatestModuleTestCoverage())
    }

    private fun calculateLatestUselessTest(): Int {
        val hotSpotTest = hotSpotRepo.queryLatestHotSpotTest(100).distinct()
        return testBadSmellRepo.getTestBadSmellByTest(hotSpotTest)
                .filter { enumContains<TestBadSmellType>(it.type) }
                .sumBy { it.size }
    }

    private fun calculateUselessPercent(): Double {
        val uselessTest = testBadSmellRepo.getTestBadSmellCount()
                .filter { enumContains<TestBadSmellType>(it.type) }
                .sumBy { it.size }.toDouble()
        val totalTest = testBadSmellRepo.getTotalTestCount().toDouble()
        return uselessTest / totalTest
    }

    private fun calculateLatestTestCoverage(): Double {
        val hotSpotFile = hotSpotRepo.queryLatestHotSpotPath(100)
                .map { it.split("/src/main/java/")[1] }
        return coverageRepo.getClassCoverageByFiles(hotSpotFile)
                .map { it.second / it.first + it.second }
                .average()
    }

    private fun calculateLatestModuleTestCoverage(): Double {
        val hotSpotFile = hotSpotRepo.queryLatestHotSpotPath(100)
                .map { it.split("/src/main/java/")[0] }.distinct()
        return coverageRepo.getClassCoverageByFiles(hotSpotFile)
                .map { it.second / it.first + it.second }
                .average()
    }
}

data class TestProtectionQualityReport(val uselessPercent: Double,
                                       val latestUselessTest: Int,
                                       val latestTestCoverage: Double,
                                       val latestModuleTestCoverage: Double) : Report {
    override fun getImprovements(): List<String> {
        return getLevel().filterValues { it == ReportLevel.NEED_IMPROVED }
                .keys.mapNotNull {
                    when (it) {
                        TestProtectionQualityReportDms.LatestModuleTestCoverage -> {
                            String.format("核心模块测试覆盖率是有%f，且存在%d个无效测试,对于核心模块，自动化测试不足，可能出现核心功能业务Bug。", latestModuleTestCoverage)
                        }
                        TestProtectionQualityReportDms.LatestTestCoverage -> {
                            String.format("核心模块测试覆盖率是有%f，且存在%d个无效测试,对于最近新增的功能，自动化测试不足，可能需要更多的手动测试来覆盖。", latestTestCoverage, latestUselessTest)
                        }
                        TestProtectionQualityReportDms.UselessPercent -> {
                            String.format("系统存无效测试占比%f, 这些测试不能有效显示功能是否遭到破坏，易误导测试人员，出现少测，漏侧现象。", uselessPercent)
                        }

                        else -> null
                    }
                }
    }

    override fun getLevel(): Map<ReportDms, ReportLevel> {
        val result = HashMap<ReportDms, ReportLevel>()
        when {
            uselessPercent > 0.05 -> {
                result[TestProtectionQualityReportDms.UselessPercent] = ReportLevel.NEED_IMPROVED
            }
            uselessPercent > 0.03 -> {
                result[TestProtectionQualityReportDms.UselessPercent] = ReportLevel.WELL
            }
            uselessPercent < 0.03 -> {
                result[TestProtectionQualityReportDms.UselessPercent] = ReportLevel.GOOD
            }
        }

        val testCoverage = latestTestCoverage - latestUselessTest * 0.01
        when {
            testCoverage > 0.8 -> {
                result[TestProtectionQualityReportDms.LatestTestCoverage] = ReportLevel.GOOD
            }
            testCoverage > 0.6 -> {
                result[TestProtectionQualityReportDms.LatestTestCoverage] = ReportLevel.WELL
            }
            testCoverage < 0.6 -> {
                result[TestProtectionQualityReportDms.LatestTestCoverage] = ReportLevel.NEED_IMPROVED
            }
        }

        val coverage = latestModuleTestCoverage - latestUselessTest * 0.01
        when {
            coverage > 0.8 -> {
                result[TestProtectionQualityReportDms.LatestModuleTestCoverage] = ReportLevel.GOOD
            }
            uselessPercent > 0.6 -> {
                result[TestProtectionQualityReportDms.LatestModuleTestCoverage] = ReportLevel.WELL
            }
            uselessPercent < 0.6 -> {
                result[TestProtectionQualityReportDms.LatestModuleTestCoverage] = ReportLevel.NEED_IMPROVED
            }
        }
        return result
    }

    override fun getComment(): ReportLevel {
        val needCount = getLevel().filterValues { it == ReportLevel.NEED_IMPROVED }.count()
        val goodCount = getLevel().filterValues { it == ReportLevel.GOOD }.count()
        val wellCount = getLevel().filterValues { it == ReportLevel.WELL }.count()
        when (max(listOf(needCount, goodCount, wellCount))) {
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

enum class TestProtectionQualityReportDms : ReportDms {
    LatestModuleTestCoverage {
        override fun getDms(): String {
            return "latestModuleTestCoverage"
        }
    },
    LatestTestCoverage {
        override fun getDms(): String {
            return "latestTestCoverage"
        }
    },
    UselessPercent {
        override fun getDms(): String {
            return "uselessPercent"
        }
    }
}

enum class TestBadSmellType {
    IgnoreTest,
    EmptyTest,
    RedundantAssertionTest,
    UnknownTest;

}

inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
    return enumValues<T>().any { it.name == name }
}
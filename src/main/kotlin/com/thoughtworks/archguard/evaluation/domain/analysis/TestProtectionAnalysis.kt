package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import com.thoughtworks.archguard.evaluation.domain.analysis.report.TestProtectionQualityReport
import com.thoughtworks.archguard.evaluation.infrastructure.enumContains
import com.thoughtworks.archguard.report.infrastructure.CoverageRepo
import com.thoughtworks.archguard.report.infrastructure.HotSpotRepo
import com.thoughtworks.archguard.report.infrastructure.TestBadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestProtectionAnalysis(@Autowired val testBadSmellRepo: TestBadSmellRepo,
                             @Autowired val coverageRepo: CoverageRepo,
                             @Autowired val hotSpotRepo: HotSpotRepo) : Analysis {
    enum class TestBadSmellType {
        IgnoreTest,
        EmptyTest,
        RedundantAssertionTest,
        UnknownTest;
    }

    override fun getName(): String {
        return "测试保护"
    }

    override fun getQualityReport(): Report? {
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
                .sumBy { it.size }
        if (uselessTest == 0) {
            return 0.0;
        }
        val totalTest = testBadSmellRepo.getTotalTestCount()

        return uselessTest.toDouble() / totalTest
    }

    private fun calculateLatestTestCoverage(): Double {
        val hotSpotFile = hotSpotRepo.queryLatestHotSpotPath(100)
                .map { it.split("/src/main/java/")[1] }
        val classCoverageByFiles = coverageRepo.getClassCoverageByFiles(hotSpotFile)
        return classCoverageByFiles
                .map {
                    if (it.second < 1) {
                        0.0
                    } else {
                        it.first / it.second
                    }
                }
                .average() * classCoverageByFiles.size / hotSpotFile.size
    }

    private fun calculateLatestModuleTestCoverage(): Double {
        val hotSpotFile = hotSpotRepo.queryLatestHotSpotPath(100)
                .map { it.split("/src/main/java/")[0] }.distinct()
        return coverageRepo.getClassCoverageByBundle(hotSpotFile)
                .map {
                    if (it.second < 1) {
                        0.0
                    } else {
                        it.first / it.second
                    }
                }
                .average()
    }


}
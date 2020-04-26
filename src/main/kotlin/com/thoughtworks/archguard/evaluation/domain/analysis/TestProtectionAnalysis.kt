package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import com.thoughtworks.archguard.report.infrastructure.CoverageRepo
import com.thoughtworks.archguard.report.infrastructure.HotSpotRepo
import com.thoughtworks.archguard.report.infrastructure.TestBadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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

data class TestProtectionQualityReport(val UselessPercent: Double,
                                       val latestUselessTest: Int,
                                       val latestTestCoverage: Double,
                                       val latestModuleTestCoverage: Double) : Report {
    override fun getImprovements(): List<String> {
        TODO("Not yet implemented")
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
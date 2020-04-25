package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import com.thoughtworks.archguard.report.infrastructure.CoverageRepo
import com.thoughtworks.archguard.report.infrastructure.TestBadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestProtectionAnalysis(@Autowired val testBadSmellRepo: TestBadSmellRepo,
                             @Autowired val coverageRepo: CoverageRepo) : Analysis {
    override fun getName(): String {
        return "测试保护"
    }

    override fun getQualityReport(): Report {
        return TestProtectionQualityReport(calculateUselessPercent())
    }

    private fun calculateUselessPercent(): Double {
        val uselessTest = testBadSmellRepo.getTestBadSmellCount()
                .filter { enumContains<TestBadSmellType>(it.type) }
                .sumBy { it.size }.toDouble()
        val totalTest = testBadSmellRepo.getTotalTestCount().toDouble()
        return uselessTest / totalTest
    }

    private fun calculateTestCoverage(): Double {
        val bundles = coverageRepo.getAll()

    }
}

data class TestProtectionQualityReport(val uselessPercent: Double) : Report {

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
package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import com.thoughtworks.archguard.report.infrastructure.TestBadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestProtectionAnalysis(@Autowired val testBadSmellRepo: TestBadSmellRepo) : Analysis {
    override fun getQualityReport(): Report {
        val uselessTest = testBadSmellRepo.getTestBadSmellCount()
                .filter { enumContains<TestBadSmellType>(it.type) }
                .sumBy { it.size }.toDouble()
        val totalTest = testBadSmellRepo.getTotalTestCount().toDouble()

        return TestProtectionQualityReport(uselessTest / totalTest)

    }

    override fun getName(): String {
        return "测试保护"
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
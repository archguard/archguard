package com.thoughtworks.archguard.evaluation_bak.domain.analysis

import com.thoughtworks.archguard.evaluation_bak.domain.analysis.report.Report
import com.thoughtworks.archguard.evaluation_bak.domain.analysis.report.TestProtectionQualityReport
import com.thoughtworks.archguard.report_bak.infrastructure.CoverageRepo
import com.thoughtworks.archguard.report_bak.infrastructure.HotSpotRepo
import com.thoughtworks.archguard.report_bak.infrastructure.TestBadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestProtectionAnalysis(@Autowired val testBadSmellRepo: TestBadSmellRepo,
                             @Autowired val coverageRepo: CoverageRepo,
                             @Autowired val hotSpotRepo: HotSpotRepo) : Analysis {
    override fun getName(): String {
        return "测试保护"
    }

    override fun getQualityReport(): Report? {
        val hotSpotTest = hotSpotRepo.queryLatestHotSpotTest(100).distinct()
        val hotSpotTestBadSmell = testBadSmellRepo.getTestBadSmellByTest(hotSpotTest)
        val hotSpoPath = hotSpotRepo.queryLatestHotSpotPath(100)
        val hotSpotFile = hotSpoPath.map { it.split("/src/main/java/")[1] }.distinct()
        val classCoverageByFiles = coverageRepo.getClassCoverageByFiles(hotSpotFile)

        val hotSpotModule = hotSpoPath.map { it.split("/src/main/java/")[0] }.distinct()
        val classCoverageByModule = coverageRepo.getClassCoverageByBundle(hotSpotModule)

        return TestProtectionQualityReport(testBadSmellRepo.getTestBadSmellCount(),
                testBadSmellRepo.getTotalTestCount(),
                hotSpotTest, hotSpotTestBadSmell,
                classCoverageByFiles, hotSpotFile,
                classCoverageByModule, hotSpotModule)
    }

}
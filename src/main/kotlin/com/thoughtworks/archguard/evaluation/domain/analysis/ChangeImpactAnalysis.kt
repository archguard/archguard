package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.ChangeImpactQualityReport
import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import com.thoughtworks.archguard.report.infrastructure.ScatteredRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class ChangeImpactAnalysis(@Autowired val scatteredRepo: ScatteredRepo) : Analysis {
    override fun getName(): String {
        return "变更影响"
    }

    override fun getQualityReport(): Report? {
        scatteredRepo.appendChangedEntriesQuantityToCommitLog()
        val time = Timestamp.valueOf(LocalDateTime.now().minusMonths(3)).time
        val scatteredCommits = scatteredRepo.findScatteredCommits(time, 8)
        val allCommits = scatteredRepo.findAllCommitLogs(time)
        return ChangeImpactQualityReport(scatteredCommits, allCommits)
    }

}

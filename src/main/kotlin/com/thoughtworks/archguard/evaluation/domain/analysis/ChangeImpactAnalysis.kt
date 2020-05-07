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
        val scatteredPercent = getScatteredPercent(3)
        return ChangeImpactQualityReport(scatteredPercent)
    }

    private fun getScatteredPercent(month: Long): Double {
        val time = Timestamp.valueOf(LocalDateTime.now().minusMonths(month)).time
        val scatteredCommits = scatteredRepo.findScatteredCommits(time, 8)
        val allCommits = scatteredRepo.findAllCommitLogs(time)
        return scatteredCommits.size.toDouble() / allCommits.size
    }

}

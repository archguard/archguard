package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import org.springframework.stereotype.Service

@Service
class ChangeImpactAnalysis : Analysis {
    override fun getName(): String {
        return "变更影响"
    }

    override fun getQualityReport(): Report? {
        return null
    }

}

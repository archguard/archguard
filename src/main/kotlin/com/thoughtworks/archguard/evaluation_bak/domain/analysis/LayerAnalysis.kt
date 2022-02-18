package com.thoughtworks.archguard.evaluation_bak.domain.analysis

import com.thoughtworks.archguard.evaluation_bak.domain.analysis.report.Report
import org.springframework.stereotype.Service

@Service
class LayerAnalysis : Analysis {
    override fun getQualityReport(): Report? {
        return null
    }

    override fun getName(): String {
        return "分层架构"
    }

}

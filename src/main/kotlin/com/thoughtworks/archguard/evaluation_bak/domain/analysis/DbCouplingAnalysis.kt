package com.thoughtworks.archguard.evaluation_bak.domain.analysis

import com.thoughtworks.archguard.evaluation_bak.domain.analysis.report.Report
import org.springframework.stereotype.Service

@Service
class DbCouplingAnalysis : Analysis {
    override fun getQualityReport(): Report? {
        return null
    }

    override fun getName(): String {
        return "数据库耦合"
    }
}
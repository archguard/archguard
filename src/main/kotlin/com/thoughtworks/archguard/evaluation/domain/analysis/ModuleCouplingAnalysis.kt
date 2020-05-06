package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.ModuleCouplingQualityReport
import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import org.springframework.stereotype.Service

@Service
class ModuleCouplingAnalysis : Analysis {
    override fun getName(): String {
        return "模块耦合"
    }

    override fun getQualityReport(): Report? {
        return ModuleCouplingQualityReport(getModuleInstability(), getModuleFanIn(), getModuleFanOut())
    }

    private fun getModuleInstability(): Double {
        return 0.0
    }

    private fun getModuleFanIn(): Int {
        return 0
    }

    private fun getModuleFanOut(): Int {
        return 0
    }
}

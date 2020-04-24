package com.thoughtworks.archguard.evaluation.domain.analysis

import org.springframework.stereotype.Service

@Service
class ModuleCouplingAnalysis : Analysis {
    override fun getQualityReport(): Report {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        return  "模块耦合"
    }

}

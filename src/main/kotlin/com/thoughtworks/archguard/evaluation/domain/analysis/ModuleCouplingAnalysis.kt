package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.ModuleCouplingQualityReport
import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import com.thoughtworks.archguard.report.infrastructure.FanInOutDBO
import com.thoughtworks.archguard.report.infrastructure.StatisticRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ModuleCouplingAnalysis(@Autowired val statisticRepo: StatisticRepo) : Analysis {
    override fun getName(): String {
        return "模块耦合"
    }

    override fun getQualityReport(): Report? {
        val moduleFanInFanOut = statisticRepo.getModuleFanInFanOut()
        val moduleCe = getModuleCe(moduleFanInFanOut)
        val moduleCa = getModuleCa(moduleFanInFanOut)
        return ModuleCouplingQualityReport(getModuleInstability(moduleCe, moduleCa),
                moduleCe, moduleCa)
    }

    private fun getModuleInstability(ce: Int, ca: Int): Double {
        return ce.toDouble() / (ce + ca)
    }

    /*离心耦合。被该包依赖的外部包的数目，该数值越大，说明该包越不独立（因为依赖了别的包），也越不稳定*/
    private fun getModuleCe(fanInFanOut: List<FanInOutDBO>): Int {
        return 0
    }

    /*向心耦合。依赖该包（包含的类）的外部包（类）的数目，该数值越大，说明该包的担当的职责越大，也就越稳定*/
    private fun getModuleCa(fanInFanOut: List<FanInOutDBO>): Int {
        return 0
    }
}

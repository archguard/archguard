package com.thoughtworks.archguard.report.domain.overview.calculator

import ModuleCouplingRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.base.BaseCalculator
import org.springframework.stereotype.Component

@Component
class ModuleHubCouplingCalculator(val moduleCouplingRepository: ModuleCouplingRepository) : BaseCalculator {
    override fun getCalculateResult(systemId: Long): BadSmellCalculateResult {
        return moduleCouplingRepository.getCouplingAboveBadSmellCalculateResult(systemId, getTypeCountLevelRanges())
    }

    private fun getTypeCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 8L until 40L
        val countRangeLevel2 = 40L until 80L
        val countRangeLevel3 = 80L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}

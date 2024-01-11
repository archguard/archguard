package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.coupling.hub.ModuleCouplingRepository
import org.archguard.smell.BadSmellLevel
import org.archguard.smell.BadSmellLevelCalculator
import org.springframework.stereotype.Component

@Component
class ModuleHubCouplingCalculator(val moduleCouplingRepository: ModuleCouplingRepository) : BadSmellLevelCalculator {
    override fun getCalculateResult(systemId: Long): BadSmellLevel {
        return moduleCouplingRepository.getCouplingAboveBadSmellCalculateResult(systemId, getTypeCountLevelRanges())
    }

    override fun getTypeCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 8L until 40L
        val countRangeLevel2 = 40L until 80L
        val countRangeLevel3 = 80L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}

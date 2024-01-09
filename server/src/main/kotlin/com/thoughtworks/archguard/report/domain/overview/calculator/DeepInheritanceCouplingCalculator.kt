package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritanceRepository
import org.archguard.smell.BadSmellResult
import org.archguard.smell.BadSmellLevelCalculator
import org.springframework.stereotype.Component

@Component
class DeepInheritanceCouplingCalculator(val deepInheritanceRepository: DeepInheritanceRepository) :
    BadSmellLevelCalculator {
    override fun getCalculateResult(systemId: Long): BadSmellResult {
        return deepInheritanceRepository.getDitAboveBadSmellCalculateResult(systemId, getTypeCountLevelRanges())
    }

    private fun getTypeCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 6L until 8L
        val countRangeLevel2 = 8L until 10L
        val countRangeLevel3 = 10L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}

package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.controller.BadSmellType
import com.thoughtworks.archguard.report.domain.deepinheritance.DeepInheritanceRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.base.BaseCouplingCalculator
import org.springframework.stereotype.Component

@Component
class DeepInheritanceCouplingCalculator(val deepInheritanceRepository: DeepInheritanceRepository) : BaseCouplingCalculator() {
    override fun getTypeCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return deepInheritanceRepository.getDitAboveBadSmellCalculateResult(systemId, getTypeCountLevelRanges())
    }

    override fun getBadSmellType(): BadSmellType {
        return BadSmellType.DEEPINHERITANCE
    }

    override fun getTypeCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 6L until 8L
        val countRangeLevel2 = 8L until 10L
        val countRangeLevel3 = 10L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}
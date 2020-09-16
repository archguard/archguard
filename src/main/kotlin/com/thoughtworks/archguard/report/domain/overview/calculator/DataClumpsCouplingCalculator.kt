package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.controller.BadSmellType
import com.thoughtworks.archguard.report.domain.dataclumps.DataClumpsRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.base.BaseCouplingCalculator
import org.springframework.stereotype.Component

@Component
class DataClumpsCouplingCalculator(val dataClumpsRepository: DataClumpsRepository) : BaseCouplingCalculator() {
    override fun getTypeCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return dataClumpsRepository.getLCOM4AboveBadSmellCalculateResult(systemId, getTypeCountLevelRanges())
    }

    override fun getBadSmellType(): BadSmellType {
        return BadSmellType.DATACLUMPS
    }

    override fun getTypeCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 2L until 4L
        val countRangeLevel2 = 4L until 8L
        val countRangeLevel3 = 8L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}

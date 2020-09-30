package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.dataclumps.DataClumpsRepository
import org.springframework.stereotype.Component

@Component
class DataClumpsCouplingCalculator(val dataClumpsRepository: DataClumpsRepository) : BadSmellLevelCalculator {
    override fun getCalculateResult(systemId: Long): BadSmellCalculateResult {
        return dataClumpsRepository.getLCOM4AboveBadSmellCalculateResult(systemId, getTypeCountLevelRanges())
    }

    private fun getTypeCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 2L until 4L
        val countRangeLevel2 = 4L until 8L
        val countRangeLevel3 = 8L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}

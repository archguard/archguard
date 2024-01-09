package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.coupling.dataclumps.DataClumpsRepository
import org.archguard.smell.BadSmellResult
import org.archguard.smell.BadSmellLevelCalculator
import org.springframework.stereotype.Component

@Component
class DataClumpsCouplingCalculator(val dataClumpsRepository: DataClumpsRepository) : BadSmellLevelCalculator {
    override fun getCalculateResult(systemId: Long): BadSmellResult {
        return dataClumpsRepository.getLCOM4AboveBadSmellCalculateResult(systemId, getTypeCountLevelRanges())
    }

    private fun getTypeCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 7L until 14L
        val countRangeLevel2 = 14L until 40L
        val countRangeLevel3 = 40L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}

package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.redundancy.DataClassRepository
import org.archguard.smell.BadSmellResult
import org.archguard.smell.BadSmellLevelCalculator
import org.springframework.stereotype.Component

@Component
class DataClassCalculator(val dataClassRepository: DataClassRepository) : BadSmellLevelCalculator {
    override fun getCalculateResult(systemId: Long): BadSmellResult {
        val allDataClassCount = dataClassRepository.getAllDataClassCount(systemId)
        return getBadSmellLevel(allDataClassCount, getLevelRanges())
    }

    private fun getBadSmellLevel(count: Long, range: Array<LongRange>): BadSmellResult {
        return when (count) {
            in range[0] -> BadSmellResult(1L, 0L, 0L)
            in range[1] -> BadSmellResult(0L, 1L, 0L)
            in range[2] -> BadSmellResult(0L, 0L, 1L)
            else -> {
                BadSmellResult(0L, 0L, 0L)
            }
        }
    }

    private fun getLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 10L until 30L
        val countRangeLevel2 = 30L until 100L
        val countRangeLevel3 = 100L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}

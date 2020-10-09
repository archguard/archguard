package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.redundancy.DataClassRepository

class DataClassCalculator(val dataClassRepository: DataClassRepository) : BadSmellLevelCalculator {
    override fun getCalculateResult(systemId: Long): BadSmellCalculateResult {
        val allDataClassCount = dataClassRepository.getAllDataClassCount(systemId)
        return getBadSmellLevel(allDataClassCount, getLevelRanges())
    }

    private fun getBadSmellLevel(count: Long, range: Array<LongRange>): BadSmellCalculateResult {
        return when (count) {
            in range[0] -> BadSmellCalculateResult(1L, 0L, 0L)
            in range[1] -> BadSmellCalculateResult(0L, 1L, 0L)
            in range[2] -> BadSmellCalculateResult(0L, 0L, 1L)
            else -> {
                BadSmellCalculateResult(0L, 0L, 0L)
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

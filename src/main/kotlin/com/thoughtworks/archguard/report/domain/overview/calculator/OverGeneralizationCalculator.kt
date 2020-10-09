package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OverGeneralizationCalculator(val overGeneralizationRepository: OverGeneralizationRepository) : BadSmellLevelCalculator {

    private val log = LoggerFactory.getLogger(OverGeneralizationCalculator::class.java)

    override fun getCalculateResult(systemId: Long): BadSmellCalculateResult {
        val overGeneralizationCount = overGeneralizationRepository.getOverGeneralizationCount(systemId)
        return getOverGeneralizationLevel(overGeneralizationCount, getOverGeneralizationCountLevelRanges())
    }

    private fun getOverGeneralizationCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 0L until 20L
        val countRangeLevel2 = 20L until 100L
        val countRangeLevel3 = 100L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }

    private fun getOverGeneralizationLevel(count: Long, range: Array<LongRange>): BadSmellCalculateResult {
        return when (count) {
            in range[0] -> BadSmellCalculateResult(1L, 0L, 0L)
            in range[1] -> BadSmellCalculateResult(0L, 1L, 0L)
            in range[2] -> BadSmellCalculateResult(0L, 0L, 1L)
            else -> {
                log.warn("OverGeneralizationCount {} Fail to match level range", count)
                BadSmellCalculateResult(0L, 0L, 0L)
            }
        }
    }

}

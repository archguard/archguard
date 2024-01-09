package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.sizing.SizingService
import org.archguard.smell.BadSmellResult
import org.archguard.smell.BadSmellLevelCalculator
import org.springframework.stereotype.Component

@Component
class ModuleOverSizingCalculator(val sizingService: SizingService) : BadSmellLevelCalculator {
    override fun getCalculateResult(systemId: Long): BadSmellResult {
        val count = sizingService.getModuleSizingSmellCount(systemId)
        return getBadSmellLevel(count, getLevelRanges())
    }

    private fun getLevelRanges(): Array<LongRange> {
        val linesRangeLevel1 = 3L until 8L
        val linesRangeLevel2 = 8L until 20L
        val linesRangeLevel3 = 20L until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

    private fun getBadSmellLevel(count: Long, range: Array<LongRange>): BadSmellResult {
        return when (count) {
            in range[0] -> BadSmellResult(count, 0L, 0L)
            in range[1] -> BadSmellResult(0L, count, 0L)
            in range[2] -> BadSmellResult(0L, 0L, count)
            else -> {
                BadSmellResult(0L, 0L, 0L)
            }
        }
    }
}

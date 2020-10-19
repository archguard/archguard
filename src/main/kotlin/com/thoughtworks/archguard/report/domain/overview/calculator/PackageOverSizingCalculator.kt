package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.sizing.SizingService
import org.springframework.stereotype.Component

@Component
class PackageOverSizingCalculator(val sizingService: SizingService) : BadSmellLevelCalculator {

    override fun getCalculateResult(systemId: Long): BadSmellCalculateResult {
        val count = sizingService.getPackageSizingSmellCount(systemId)
        return getBadSmellLevel(count, getLevelRanges())
    }

    private fun getLevelRanges(): Array<LongRange> {
        val linesRangeLevel1 = 3L until 8L
        val linesRangeLevel2 = 8L until 20L
        val linesRangeLevel3 = 20L until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

    private fun getBadSmellLevel(count: Long, range: Array<LongRange>): BadSmellCalculateResult {
        return when (count) {
            in range[0] -> BadSmellCalculateResult(count, 0L, 0L)
            in range[1] -> BadSmellCalculateResult(0L, count, 0L)
            in range[2] -> BadSmellCalculateResult(0L, 0L, count)
            else -> {
                BadSmellCalculateResult(0L, 0L, 0L)
            }
        }
    }

}

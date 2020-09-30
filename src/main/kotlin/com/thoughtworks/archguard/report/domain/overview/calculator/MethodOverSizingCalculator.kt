package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.stereotype.Component

@Component
class MethodOverSizingCalculator(val sizingRepository: SizingRepository) : BadSmellLevelCalculator {

    override fun getCalculateResult(systemId: Long): BadSmellCalculateResult {
        return sizingRepository.getMethodSizingAboveLineBadSmellResult(systemId, getLineCountLevelRanges())
    }

    private fun getLineCountLevelRanges(): Array<LongRange> {
        val linesRangeLevel1 = 30L until 40L
        val linesRangeLevel2 = 40L until 50L
        val linesRangeLevel3 = 50L until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

}

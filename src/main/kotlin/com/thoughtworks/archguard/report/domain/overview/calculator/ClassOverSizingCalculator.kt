package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.stereotype.Component

@Component
class ClassOverSizingCalculator(val sizingRepository: SizingRepository) : BadSmellLevelCalculator {

    override fun getCalculateResult(systemId: Long): BadSmellCalculateResult {
        val aboveMethodBadSmellResult = sizingRepository.getClassSizingListAboveMethodCountBadSmellResult(systemId, getTypeCountLevelRanges())
        val aboveLineBadSmellResult = sizingRepository.getClassSizingAboveLineBadSmellResult(systemId, getLineCountLevelRanges())
        return aboveMethodBadSmellResult.plus(aboveLineBadSmellResult);
    }

    private fun getLineCountLevelRanges(): Array<LongRange> {
        val linesRangeLevel1 = 600L until 1000L
        val linesRangeLevel2 = 1000L until 2000L
        val linesRangeLevel3 = 2000L until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

    private fun getTypeCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 20L until 40L
        val countRangeLevel2 = 40L until 60L
        val countRangeLevel3 = 60L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}

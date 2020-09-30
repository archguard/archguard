package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.overview.calculator.base.BaseCalculator
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.stereotype.Component

@Component
class ModuleOverSizingCalculator(val sizingRepository: SizingRepository) : BaseCalculator() {
    override fun getTypeCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return sizingRepository.getModuleSizingListAbovePackageCountBadSmellResult(systemId, getTypeCountLevelRanges())
    }

    override fun getLineCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return sizingRepository.getModuleSizingAboveLineBadSmellResult(systemId, getLineCountLevelRanges())
    }

    private fun getLineCountLevelRanges(): Array<LongRange> {
        val linesRangeLevel1 = 24_0000L until 30_0000L
        val linesRangeLevel2 = 30_0000L until 50_0000L
        val linesRangeLevel3 = 50_0000L until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

    private fun getTypeCountLevelRanges(): Array<LongRange> {
        val moduleCountRangeLevel1 = 20L until 40L
        val moduleCountRangeLevel2 = 40L until 60L
        val moduleCountRangeLevel3 = 60L until Long.MAX_VALUE
        return arrayOf(moduleCountRangeLevel1, moduleCountRangeLevel2, moduleCountRangeLevel3)
    }


}

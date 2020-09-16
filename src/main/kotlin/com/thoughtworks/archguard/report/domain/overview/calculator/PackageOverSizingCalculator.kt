package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.controller.BadSmellType
import com.thoughtworks.archguard.report.domain.overview.calculator.base.BaseOverSizingCalculator
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.stereotype.Component

@Component
class PackageOverSizingCalculator(val sizingRepository: SizingRepository) : BaseOverSizingCalculator() {

    override fun getTypeCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return sizingRepository.getPackageSizingListAboveClassCountBadSmellResult(systemId, getTypeCountLevelRanges())
    }

    override fun getLineCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return sizingRepository.getPackageSizingAboveLineBadSmellResult(systemId, getLineCountLevelRanges())
    }

    override fun getBadSmellType(): BadSmellType {
        return BadSmellType.SIZINGPACKAGE
    }

    override fun getLineCountLevelRanges(): Array<LongRange> {
        val linesRangeLevel1 = 1_2000L until 2_0000L
        val linesRangeLevel2 = 2_0000L until 3_0000L
        val linesRangeLevel3 = 3_0000L until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

    override fun getTypeCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 20L until 40L
        val countRangeLevel2 = 40L until 60L
        val countRangeLevel3 = 60L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }

}

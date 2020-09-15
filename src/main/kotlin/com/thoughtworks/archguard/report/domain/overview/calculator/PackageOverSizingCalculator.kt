package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.overview.BadSmell
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class PackageOverSizingCalculator(val sizingRepository: SizingRepository) : BaseOverSizingCalculator() {

    @Value("\${threshold.package.class.count}")
    private val packageClassCountSizingThreshold: Int = 0

    @Value("\${threshold.package.line}")
    private val packageSizingLineThreshold: Int = 0

    override fun getCount(systemId: Long): Long {
        return sizingRepository.getPackageSizingListAboveClassCountThresholdCount(systemId, packageClassCountSizingThreshold)
    }

    override fun getLineCount(systemId: Long): Long {
        return sizingRepository.getPackageSizingAboveLineThresholdCount(systemId, packageSizingLineThreshold)
    }

    override fun getBadSmellType(): BadSmell {
        return BadSmell.PACKAGE_OVER_SIZING
    }

    override fun getLineCountLevelRanges(): Array<LongRange> {
        val linesRangeLevel1 = 1_2000L until 2_0000L
        val linesRangeLevel2 = 2_0000L until 3_0000L
        val linesRangeLevel3 = 3_0000L until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

    override fun getCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 20L until 40L
        val countRangeLevel2 = 40L until 60L
        val countRangeLevel3 = 60L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }

}

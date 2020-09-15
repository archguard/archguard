package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.overview.BadSmell
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ModuleOverSizingCalculator(val sizingRepository: SizingRepository) : BaseOverSizingCalculator() {

    @Value("\${threshold.module.package.count}")
    private val modulePackageCountSizingThreshold: Int = 0

    @Value("\${threshold.module.line}")
    private val moduleSizingLineThreshold: Int = 0

    override fun getLineCountLevelRanges(): Array<Any> {
        val linesRangeLevel1 = 24_000 until 30_000
        val linesRangeLevel2 = 30_000 until 50_000
        val linesRangeLevel3 = 50_000 until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

    override fun getCountLevelRanges(): Array<Any> {
        val moduleCountRangeLevel1 = 20 until 40
        val moduleCountRangeLevel2 = 40 until 60
        val moduleCountRangeLevel3 = 60 until Long.MAX_VALUE
        return arrayOf(moduleCountRangeLevel1, moduleCountRangeLevel2, moduleCountRangeLevel3)
    }

    override fun getCount(systemId: Long): Long {
        return sizingRepository.getModuleSizingListAbovePackageCountThresholdCount(systemId, modulePackageCountSizingThreshold)
    }

    override fun getLineCount(systemId: Long): Long {
        return sizingRepository.getModuleSizingAboveLineThresholdCount(systemId, moduleSizingLineThreshold)
    }

    override fun getBadSmellType(): BadSmell {
        return BadSmell.MODULE_OVER_SIZING
    }

}

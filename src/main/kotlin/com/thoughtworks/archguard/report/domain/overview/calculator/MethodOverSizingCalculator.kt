package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.overview.calculator.base.BaseOverSizingCalculator
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.stereotype.Component

@Component
class MethodOverSizingCalculator(val sizingRepository: SizingRepository) : BaseOverSizingCalculator() {

    override fun getTypeCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return BadSmellCalculateResult()
    }

    override fun getLineCountCalculateResult(systemId: Long): BadSmellCalculateResult {
        return sizingRepository.getMethodSizingAboveLineBadSmellResult(systemId, getLineCountLevelRanges())
    }

    override fun getBadSmellType(): BadSmellType {
        return BadSmellType.SIZINGMETHOD
    }

    override fun getLineCountLevelRanges(): Array<LongRange> {
        val linesRangeLevel1 = 30L until 40L
        val linesRangeLevel2 = 40L until 50L
        val linesRangeLevel3 = 50L until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

    override fun getTypeCountLevelRanges(): Array<LongRange> {
        TODO("Not yet implemented")
    }
}

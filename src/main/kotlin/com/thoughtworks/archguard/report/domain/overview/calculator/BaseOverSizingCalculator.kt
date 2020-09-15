package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.overview.BadSmell
import com.thoughtworks.archguard.report.domain.overview.BadSmellCategory
import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem

abstract class BaseOverSizingCalculator {

    abstract fun getCount(systemId: Long): BadSmellCalculateResult
    abstract fun getLineCount(systemId: Long): BadSmellCalculateResult
    abstract fun getBadSmellType(): BadSmell

    abstract fun getLineCountLevelRanges(): Array<LongRange>
    abstract fun getCountLevelRanges(): Array<LongRange>

    fun getOverSizingOverviewItem(systemId: Long): BadSmellOverviewItem {
        val count = getCount(systemId)
        val lineCount = getLineCount(systemId)
        val result = count.plus(lineCount)
        return BadSmellOverviewItem(getBadSmellType(), BadSmellCategory.OVER_SIZING, result.calculateLevel(), result.totalCount())
    }

}

package com.thoughtworks.archguard.report.domain.overview.calculator.base

import com.thoughtworks.archguard.report.domain.overview.BadSmell
import com.thoughtworks.archguard.report.domain.overview.BadSmellCategory
import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult

abstract class BaseCalculator {

    abstract fun getTypeCountCalculateResult(systemId: Long): BadSmellCalculateResult
    abstract fun getLineCountCalculateResult(systemId: Long): BadSmellCalculateResult
    abstract fun getBadSmellType(): BadSmell
    abstract fun getBadSmellCategory(): BadSmellCategory

    abstract fun getLineCountLevelRanges(): Array<LongRange>
    abstract fun getTypeCountLevelRanges(): Array<LongRange>

    fun getOverSizingOverviewItem(systemId: Long): BadSmellOverviewItem {
        val count = getTypeCountCalculateResult(systemId)
        val lineCount = getLineCountCalculateResult(systemId)
        val result = count.plus(lineCount)
        return BadSmellOverviewItem(getBadSmellType(), getBadSmellCategory(), result.calculateLevel(), result.totalCount())
    }

}

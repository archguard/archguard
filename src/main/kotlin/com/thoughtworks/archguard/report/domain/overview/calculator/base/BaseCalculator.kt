package com.thoughtworks.archguard.report.domain.overview.calculator.base

import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult

abstract class BaseCalculator {

    abstract fun getTypeCountCalculateResult(systemId: Long): BadSmellCalculateResult
    abstract fun getLineCountCalculateResult(systemId: Long): BadSmellCalculateResult

    fun getOverSizingOverviewItem(systemId: Long, badSmellType: BadSmellType): BadSmellOverviewItem {
        val count = getTypeCountCalculateResult(systemId)
        val lineCount = getLineCountCalculateResult(systemId)
        val result = count.plus(lineCount)
        return BadSmellOverviewItem(badSmellType, result.calculateLevel(), result.totalCount())
    }

}

package com.thoughtworks.archguard.report.domain.overview.calculator.base

import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult

interface BaseCalculator {
    fun getCalculateResult(systemId: Long): BadSmellCalculateResult

    fun getBadSmellOverviewItem(systemId: Long, badSmellType: BadSmellType): BadSmellOverviewItem {
        val result = getCalculateResult(systemId)
        return BadSmellOverviewItem(badSmellType, result.calculateLevel(), result.totalCount())
    }

}

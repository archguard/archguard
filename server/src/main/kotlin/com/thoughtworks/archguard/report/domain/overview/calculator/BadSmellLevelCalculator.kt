package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem
import org.archguard.smell.BadSmellCalculateResult

interface BadSmellLevelCalculator {
    fun getCalculateResult(systemId: Long): BadSmellCalculateResult

    fun getBadSmellOverviewItem(systemId: Long, badSmellType: BadSmellType): BadSmellOverviewItem {
        val result = getCalculateResult(systemId)
        return BadSmellOverviewItem(badSmellType, BadSmellCalculator.calculateLevel(result), result.totalCount())
    }
}

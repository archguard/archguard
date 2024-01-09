package org.archguard.smell

interface BadSmellLevelCalculator {
    fun getCalculateResult(systemId: Long): BadSmellResult

    fun getBadSmellOverviewItem(systemId: Long, badSmellType: BadSmellType): BadSmellOverviewItem {
        val result = getCalculateResult(systemId)
        return BadSmellOverviewItem(badSmellType, BadSmellCalculator.calculateLevel(result), result.totalCount())
    }
}

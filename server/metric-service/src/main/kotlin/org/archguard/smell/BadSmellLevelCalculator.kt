package org.archguard.smell

/**
 * The `BadSmellLevelCalculator` interface provides methods to calculate the bad smell level of a system and retrieve an overview of the bad smells.
 *
 * This interface defines the following methods:
 * - `getCalculateResult(systemId: Long)`: Calculates the bad smell level of the system identified by the given `systemId`.
 * - `getBadSmellOverviewItem(systemId: Long, badSmellType: BadSmellType)`: Retrieves an overview item for the specified `badSmellType` in the system identified by the given `systemId`.
 *
 * Example usage:
 * ```
 * val calculator: BadSmellLevelCalculator = ...
 * val systemId = 12345L
 * val result = calculator.getCalculateResult(systemId)
 * val overviewItem = calculator.getBadSmellOverviewItem(systemId, BadSmellType.CODE_SMELL)
 * ```
 *
 * @see BadSmellLevel
 * @see BadSmellOverviewItem
 * @see BadSmellType
 */
interface BadSmellLevelCalculator {
    fun getCalculateResult(systemId: Long): BadSmellLevel

    fun getBadSmellOverviewItem(systemId: Long, badSmellType: BadSmellType): BadSmellOverviewItem {
        val result = getCalculateResult(systemId)
        return BadSmellOverviewItem(badSmellType, BadSmellCalculator.calculateLevel(result), result.totalCount())
    }
}

package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.overview.BadSmell
import com.thoughtworks.archguard.report.domain.overview.BadSmellCategory
import com.thoughtworks.archguard.report.domain.overview.BadSmellLevel
import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewItem

abstract class BaseOverSizingCalculator {

    abstract fun getCount(systemId: Long): Long
    abstract fun getLineCount(systemId: Long): Long
    abstract fun getBadSmellType(): BadSmell

    abstract fun getLineCountLevelRanges(): Array<LongRange>
    abstract fun getCountLevelRanges(): Array<LongRange>

    fun getOverSizingOverviewItem(systemId: Long): BadSmellOverviewItem {
        val count = getCount(systemId)
        val lineCount = getLineCount(systemId)
        val level = calculateLevel(count, lineCount)
        return BadSmellOverviewItem(getBadSmellType(), BadSmellCategory.OVER_SIZING, level, count + lineCount)
    }


    private fun calculateLevel(count: Long, lines: Long): BadSmellLevel {
        var level1 = 0
        var level2 = 0
        var level3 = 0

        val linesRanges = getLineCountLevelRanges()

        when (lines) {
            in linesRanges[0] -> level1++
            in linesRanges[1] -> level2++
            in linesRanges[2] -> level3++
        }

        val countRanges = getCountLevelRanges()
        if (countRanges.size == 3) {
            when (count) {
                in countRanges[0] -> level1++
                in countRanges[1] -> level2++
                in countRanges[2] -> level3++
            }
        }

        return getLevelResult(level1, level2, level3)
    }

    private fun getLevelResult(level1: Int, level2: Int, level3: Int): BadSmellLevel {
        return if (level1 == 0 && level2 == 0 && level3 == 0) {
            BadSmellLevel.A
        } else if (level1 > 0 && level2 == 0 && level3 == 0) {
            BadSmellLevel.B
        } else if (level2 > 0 && level3 == 0) {
            BadSmellLevel.C
        } else if (level3 > 0) {
            BadSmellLevel.D
        } else {
            BadSmellLevel.A
        }
    }
}

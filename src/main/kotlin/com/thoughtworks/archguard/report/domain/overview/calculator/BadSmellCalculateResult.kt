package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.overview.BadSmellLevel

class BadSmellCalculateResult(var level1: Long = 0, var level2: Long = 0, var level3: Long = 0) {
    fun totalCount(): Long {
        return level1 + level2 + level3
    }

    fun plus(other: BadSmellCalculateResult): BadSmellCalculateResult {
        return BadSmellCalculateResult(this.level1 + other.level1, this.level2 + other.level2, this.level3 + other.level3)
    }

    fun calculateLevel(): BadSmellLevel {
        return if (level1 == 0L && level2 == 0L && level3 == 0L) {
            BadSmellLevel.A
        } else if (level1 > 0 && level2 == 0L && level3 == 0L) {
            BadSmellLevel.B
        } else if (level2 > 0 && level3 == 0L) {
            BadSmellLevel.C
        } else if (level3 > 0) {
            BadSmellLevel.D
        } else {
            BadSmellLevel.A
        }
    }
}

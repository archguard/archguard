package org.archguard.smell

object BadSmellCalculator {
    fun calculateLevel(badSmellCalculateResult: BadSmellCalculateResult): BadSmellLevel {
        return if (badSmellCalculateResult.level1 == 0L && badSmellCalculateResult.level2 == 0L && badSmellCalculateResult.level3 == 0L) {
            BadSmellLevel.A
        } else if (badSmellCalculateResult.level1 > 0 && badSmellCalculateResult.level2 == 0L && badSmellCalculateResult.level3 == 0L) {
            BadSmellLevel.B
        } else if (badSmellCalculateResult.level2 > 0 && badSmellCalculateResult.level3 == 0L) {
            BadSmellLevel.C
        } else if (badSmellCalculateResult.level3 > 0) {
            BadSmellLevel.D
        } else {
            BadSmellLevel.A
        }
    }
}


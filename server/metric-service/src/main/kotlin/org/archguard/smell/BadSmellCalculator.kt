package org.archguard.smell

object BadSmellCalculator {
    fun calculateLevel(badSmellResult: BadSmellResult): BadSmellLevel {
        return if (badSmellResult.level1 == 0L && badSmellResult.level2 == 0L && badSmellResult.level3 == 0L) {
            BadSmellLevel.A
        } else if (badSmellResult.level1 > 0 && badSmellResult.level2 == 0L && badSmellResult.level3 == 0L) {
            BadSmellLevel.B
        } else if (badSmellResult.level2 > 0 && badSmellResult.level3 == 0L) {
            BadSmellLevel.C
        } else if (badSmellResult.level3 > 0) {
            BadSmellLevel.D
        } else {
            BadSmellLevel.A
        }
    }
}


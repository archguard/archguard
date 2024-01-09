package org.archguard.smell

object BadSmellCalculator {
    fun calculateLevel(badSmellLevel: BadSmellLevel): BadSmellLevelType {
        return if (badSmellLevel.level1 == 0L && badSmellLevel.level2 == 0L && badSmellLevel.level3 == 0L) {
            BadSmellLevelType.A
        } else if (badSmellLevel.level1 > 0 && badSmellLevel.level2 == 0L && badSmellLevel.level3 == 0L) {
            BadSmellLevelType.B
        } else if (badSmellLevel.level2 > 0 && badSmellLevel.level3 == 0L) {
            BadSmellLevelType.C
        } else if (badSmellLevel.level3 > 0) {
            BadSmellLevelType.D
        } else {
            BadSmellLevelType.A
        }
    }
}


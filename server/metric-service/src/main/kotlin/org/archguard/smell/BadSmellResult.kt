package org.archguard.smell

class BadSmellResult(var level1: Long = 0, var level2: Long = 0, var level3: Long = 0) {
    fun totalCount(): Long {
        return level1 + level2 + level3
    }

    fun plus(other: BadSmellResult): BadSmellResult {
        return BadSmellResult(this.level1 + other.level1, this.level2 + other.level2, this.level3 + other.level3)
    }
}


package org.archguard.smell

/**
 * The `BadSmellLevel` class represents the levels of bad smell in a certain context.
 * It provides methods to calculate the total count of bad smell levels and to add two instances of `BadSmellLevel` together.
 *
 * @property level1 The level of bad smell 1.
 * @property level2 The level of bad smell 2.
 * @property level3 The level of bad smell 3.
 *
 * @constructor Creates a `BadSmellLevel` instance with the specified levels of bad smell.
 */
class BadSmellLevel(var level1: Long = 0, var level2: Long = 0, var level3: Long = 0) {
    fun totalCount(): Long {
        return level1 + level2 + level3
    }

    fun plus(other: BadSmellLevel): BadSmellLevel {
        return BadSmellLevel(this.level1 + other.level1, this.level2 + other.level2, this.level3 + other.level3)
    }
}


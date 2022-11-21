package org.archguard.scanner.cost.ignore

import org.archguard.scanner.cost.ignore.Pattern.Companion.newPatternForEqualizedPath

const val initials: String = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ."

class InitialPatternHolder(
    private val patterns: InitialPatterns = InitialPatterns(),
    private val otherPatterns: Patterns = Patterns()
) {
    fun add(pattern: String) {
        val trimedPattern = pattern.trimStart('/')

        if (trimedPattern == "") return

        if (initials.indexOf(trimedPattern[0]) != -1) {
            patterns.set(trimedPattern[0].code.toByte(), newPatternForEqualizedPath(pattern))
        } else {
            otherPatterns.add(newPatternForEqualizedPath(pattern))
        }
    }

    fun match(path: String, isDir: Boolean): Boolean {
        if (patterns.size() == 0 && otherPatterns.size() == 0) {
            return false
        }
        if (patterns.get(path[0].code.toByte())?.match(path, isDir) == true) {
            return true
        }

        return otherPatterns.match(path, isDir)
    }
}

class InitialPatterns {
    private val m: MutableMap<Byte, Patterns> = mutableMapOf()

    fun set(initial: Byte, pattern: Pattern) {
        if (m.containsKey(initial)) {
            m[initial]!!.add(pattern)
        } else {
            val patterns = Patterns()
            patterns.add(pattern)
            m[initial] = patterns
        }
    }

    fun get(initial: Byte): Patterns? {
        return m[initial]
    }

    fun size(): Int {
        return m.size
    }
}
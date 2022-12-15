package org.archguard.scanner.analyser.ignore

class Patterns {
    private val patterns = mutableListOf<Pattern>()

    fun add(pattern: Pattern) {
        patterns.add(pattern)
    }

    fun size(): Int {
        return patterns.size
    }

    fun match(path: String, isDir: Boolean): Boolean {
        for (p in patterns) {
            if (p.match(path, isDir)) {
                return true
            }
        }
        return false
    }
}
package org.archguard.scanner.analyser.ignore

import org.archguard.scanner.analyser.ignore.Pattern.Companion.newPattern

class FullScanPatterns(private val absolute: Patterns = Patterns(), private val relative: Patterns = Patterns()) {
    fun add(pattern: String) {
        if (pattern.startsWith("/")) {
            absolute.add(newPattern(pattern))
        } else {
            relative.add(newPattern(pattern))
        }
    }

    fun match(path: String, isDir: Boolean): Boolean {
        if (absolute.match(path, isDir)) {
            return true
        }
        return relative.match(path, isDir)
    }
}

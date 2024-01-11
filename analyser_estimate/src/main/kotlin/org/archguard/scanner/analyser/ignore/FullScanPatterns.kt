package org.archguard.scanner.analyser.ignore

import org.archguard.scanner.analyser.ignore.Pattern.Companion.newPattern

/**
 * Represents a class that contains patterns for full scan.
 *
 * The `FullScanPatterns` class allows you to add patterns for both absolute and relative paths. It provides methods
 * to add patterns and match paths against the added patterns.
 *
 * @property absolute The patterns for absolute paths.
 * @property relative The patterns for relative paths.
 */
class FullScanPatterns(val absolute: Patterns = Patterns(), val relative: Patterns = Patterns()) {
    /**
     * Adds a pattern to the appropriate pattern collection based on whether the pattern starts with a forward
     * slash ("/") or not.
     *
     * If the pattern starts with a forward slash ("/"), it is added to the `absolute` pattern collection. Otherwise,
     * it is added to the `relative` pattern collection.
     *
     * @param pattern The pattern to be added.
     */
    fun add(pattern: String) {
        if (pattern.startsWith("/")) {
            absolute.add(newPattern(pattern))
        } else {
            relative.add(newPattern(pattern))
        }
    }

    /**
     * Matches the given path against the added patterns.
     *
     * The `match` method checks if the given path matches any of the patterns in the `absolute` or `relative` pattern
     * collections. It returns `true` if a match is found, otherwise it returns `false`.
     *
     * @param path The path to be matched.
     * @param isDir Indicates whether the path represents a directory or not.
     * @return `true` if a match is found, `false` otherwise.
     */
    fun match(path: String, isDir: Boolean): Boolean {
        if (absolute.match(path, isDir)) {
            return true
        }
        return relative.match(path, isDir)
    }
}

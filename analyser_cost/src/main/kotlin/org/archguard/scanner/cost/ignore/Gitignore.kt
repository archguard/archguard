package org.archguard.scanner.cost.ignore

import java.io.File

interface IgnoreMatcher {
    fun match(path: String, isDir: Boolean): Boolean
}

class Gitignore(gitignore: String, base: String = "") : IgnoreMatcher {
    private val ignorePatterns: ScanStrategy = IndexScanPatterns()
    private val acceptPatterns: ScanStrategy = IndexScanPatterns()
    private val path: String

    init {
        val file = File(gitignore)
        path = if (base.isEmpty()) {
            file.parent
        } else {
            base
        }
        file.forEachLine { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                return@forEachLine
            }
            if (trimmedLine.startsWith("!")) {
                acceptPatterns.add(trimmedLine.substring(1))
            } else {
                ignorePatterns.add(trimmedLine)
            }
        }
    }

    override fun match(path: String, isDir: Boolean): Boolean {
        val relativePath = File(path).relativeTo(File(this.path)).path
        if (acceptPatterns.match(relativePath, isDir)) {
            return false
        }
        return ignorePatterns.match(relativePath, isDir)
    }
}
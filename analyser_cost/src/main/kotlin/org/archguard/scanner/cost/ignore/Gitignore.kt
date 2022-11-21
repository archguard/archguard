package org.archguard.scanner.cost.ignore

import java.io.File

interface IgnoreMatcher {
    fun match(path: String, isDir: Boolean): Boolean
}

class Gitignore(val path: String) : IgnoreMatcher {
    private val ignorePatterns: ScanStrategy = IndexScanPatterns()
    private val acceptPatterns: ScanStrategy = IndexScanPatterns()

    override fun match(path: String, isDir: Boolean): Boolean {
        val relativePath = File(path).relativeTo(File(this.path)).path
        if (acceptPatterns.match(relativePath, isDir)) {
            return false
        }
        return ignorePatterns.match(relativePath, isDir)
    }

    companion object {
        fun create(gitignore: String, base: String = ""): IgnoreMatcher {
            val path: String = if (base != "") {
                base
            } else {
                File(gitignore).parent
            }

            val file = File(gitignore)
            return newGitIgnoreFromReader(path, file.readLines())
        }

        fun newGitIgnoreFromReader(path: String, lines: List<String>): Gitignore {
            val g = Gitignore(path)
            lines.forEach { line ->
                val trimmed = line.trim()
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    return@forEach
                }

                if (trimmed.startsWith("!")) {
                    g.acceptPatterns.add(trimmed.substring(1))
                } else {
                    g.ignorePatterns.add(trimmed)
                }
            }
            return g
        }
    }
}
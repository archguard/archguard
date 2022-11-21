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
        fun create(gitignore: String, vararg base: String): Gitignore? {
            val path: String = if (base.isNotEmpty()) {
                base[0]
            } else {
                File(gitignore).parent
            }

            val ignoreFile = File(gitignore)

            if (!ignoreFile.exists()) {
                println("Ignore file $gitignore does not exist")
                return null
            }

            return fromReader(path, ignoreFile.readLines())
        }

        fun fromReader(path: String, lines: List<String>): Gitignore {
            val g = Gitignore(path)
            for (line in lines) {
                val trimmed = line.trim()
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue
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
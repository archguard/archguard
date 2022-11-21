package org.archguard.scanner.cost.ignore

import java.nio.file.FileSystem
import java.nio.file.FileSystems

interface IgnorePathMatcher {
    fun match(path: String): Boolean
}

class SimpleMatcherIgnore(val path: String) : IgnorePathMatcher {
    override fun match(path: String): Boolean {
        return this.path == path
    }
}

var fileSystem: FileSystem = FileSystems.getDefault()

class FilepathMatcherIgnore(val path: String) : IgnorePathMatcher {
    override fun match(path: String): Boolean {
        return fileSystem.getPathMatcher("glob:${this.path}").matches(fileSystem.getPath(path))
    }
}
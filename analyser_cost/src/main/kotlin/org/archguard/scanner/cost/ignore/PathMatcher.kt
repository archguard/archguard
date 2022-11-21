package org.archguard.scanner.cost.ignore

import com.google.re2j.Pattern as Re2jPattern

interface PathMatcher {
    fun match(path: String): Boolean
}

class SimpleMatcher(val path: String) : PathMatcher {
    override fun match(path: String): Boolean {
        return this.path == path
    }
}

class FilepathMatcher(val path: String) : PathMatcher {
    override fun match(path: String): Boolean {
        try {
            return path.matches(Regex("^${this.path}$"))
        } catch (e: Exception) {
            println("Invalid pattern: $path")
            return false
        }
    }
}

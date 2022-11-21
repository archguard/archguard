package org.archguard.scanner.cost.ignore

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
        return path.matches(this.path.toRegex())
    }
}

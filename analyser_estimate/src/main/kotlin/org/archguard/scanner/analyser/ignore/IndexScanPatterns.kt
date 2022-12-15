package org.archguard.scanner.analyser.ignore


interface ScanStrategy {
    fun add(pattern: String)
    fun match(path: String, isDir: Boolean): Boolean
}

class IndexScanPatterns(
    private val absolute: DepthPatternHolder = DepthPatternHolder(Order.ASC),
    private val relative: DepthPatternHolder = DepthPatternHolder(Order.DESC)
): ScanStrategy {

    override fun add(pattern: String) {
        if (pattern.startsWith("/")) {
            absolute.add(pattern)
        } else {
            relative.add(pattern)
        }
    }

    override fun match(path: String, isDir: Boolean): Boolean {
        if (absolute.match(path, isDir)) {
            return true
        }
        return relative.match(path, isDir)
    }
}

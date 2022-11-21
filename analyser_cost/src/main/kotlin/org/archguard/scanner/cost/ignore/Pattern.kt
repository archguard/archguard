package org.archguard.scanner.cost.ignore

class Pattern(
    var hasRootPrefix: Boolean = false,
    var hasDirSuffix: Boolean = false,
    var pathDepth: Int = 0,
    var matcher: IgnorePathMatcher,
    var onlyEqualizedPath: Boolean = false
) {
    fun match(path: String, isDir: Boolean): Boolean {
        if (hasDirSuffix && !isDir) {
            return false
        }
        val targetPath: String = if (hasRootPrefix || onlyEqualizedPath) {
            path
        } else {
            equalizeDepth(path)
        }

        return matcher.match(targetPath)
    }

    fun equalizeDepth(path: String): String {
        val (equalizedPath, _) = cutLastN(path, pathDepth + 1)
        return equalizedPath
    }

    companion object {
        fun newPattern(path: String): Pattern {
            val hasRootPrefix = path[0] == '/'
            val hasDirSuffix = path[path.length - 1] == '/'

            var pathDepth = 0
            if (!hasRootPrefix) {
                pathDepth = path.count { it == '/' }
            }

            val matcher: IgnorePathMatcher
            val matchingPath = path.trim('/')
            if (hasMeta(path)) {
                matcher = FilepathMatcherIgnore(path = matchingPath)
            } else {
                matcher = SimpleMatcherIgnore(path = matchingPath)
            }

            return Pattern(
                hasRootPrefix = hasRootPrefix,
                hasDirSuffix = hasDirSuffix,
                pathDepth = pathDepth,
                matcher = matcher,
            )
        }

        fun newPatternForEqualizedPath(path: String): Pattern {
            val pattern = newPattern(path)
            pattern.onlyEqualizedPath = true
            return pattern
        }
    }
}
package org.archguard.scanner.estimate.ignore

enum class Order {
    ASC,
    DESC
}

class DepthPatternHolder(private val order: Order) {
    private val patterns = DepthPatterns()

    fun add(pattern: String) {
        val count = pattern.trim('/').count { it == '/' }
        patterns.set(count + 1, pattern)
    }

    fun match(path: String, isDir: Boolean): Boolean {
        if (patterns.size() == 0) {
            return false
        }

        for (depth in 1..Int.MAX_VALUE) {
            val (part, isLast) = if (order == Order.ASC) {
                cutN(path, depth)
            } else {
                cutLastN(path, depth)
            }
            val isDirCurrent = if (isLast) {
                isDir
            } else {
                false
            }
            val patterns = patterns.get(depth)
            if (patterns != null && patterns.match(part, isDirCurrent)) {
                return true
            }
            if (isLast) {
                break
            }
        }
        return false
    }
}

class DepthPatterns {
    private val m = mutableMapOf<Int, InitialPatternHolder>()

    fun set(depth: Int, pattern: String) {
        val ps = m[depth]
        if (ps != null) {
            ps.add(pattern)
        } else {
            val holder = InitialPatternHolder()
            holder.add(pattern)
            m[depth] = holder
        }
    }

    fun get(depth: Int): InitialPatternHolder? {
        return m[depth]
    }

    fun size(): Int {
        return m.size
    }
}
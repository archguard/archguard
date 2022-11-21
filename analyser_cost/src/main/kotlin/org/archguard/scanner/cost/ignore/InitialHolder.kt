package org.archguard.scanner.cost.ignore

import org.archguard.scanner.cost.ignore.Pattern.Companion.newPatternForEqualizedPath

// const initials = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ."
//
//type initialPatternHolder struct {
//	patterns      initialPatterns
//	otherPatterns *patterns
//}
//
//func newInitialPatternHolder() initialPatternHolder {
//	return initialPatternHolder{
//		patterns:      initialPatterns{m: map[byte]*patterns{}},
//		otherPatterns: &patterns{},
//	}
//}
//
//func (h *initialPatternHolder) add(pattern string) {
//	trimedPattern := strings.TrimPrefix(pattern, "/")
//
//	if trimedPattern == "" {
//		return
//	}
//
//	if strings.IndexAny(trimedPattern[0:1], initials) != -1 {
//		h.patterns.set(trimedPattern[0], newPatternForEqualizedPath(pattern))
//	} else {
//		h.otherPatterns.add(newPatternForEqualizedPath(pattern))
//	}
//}
//
//func (h initialPatternHolder) match(path string, isDir bool) bool {
//	if h.patterns.size() == 0 && h.otherPatterns.size() == 0 {
//		return false
//	}
//	if patterns, ok := h.patterns.get(path[0]); ok {
//		if patterns.match(path, isDir) {
//			return true
//		}
//	}
//	return h.otherPatterns.match(path, isDir)
//}
const val initials: String = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ."

class InitialPatternHolder(
    private val patterns: MutableMap<Byte, Patterns> = mutableMapOf(),
    private val otherPatterns: Patterns = Patterns()
) {

    fun add(pattern: String) {
        val trimedPattern = pattern.trimStart('/')

        if (trimedPattern == "") return

        if (initials.indexOf(trimedPattern[0]) != -1) {
            patterns.getOrPut(trimedPattern[0].code.toByte()) { Patterns() }.add(newPatternForEqualizedPath(pattern))
        } else {
            otherPatterns.add(newPatternForEqualizedPath(pattern))
        }
    }

    fun match(path: String, isDir: Boolean): Boolean {
        if (patterns.isEmpty() && otherPatterns.size() == 0) {
            return false
        }
        if (patterns.containsKey(path[0].code.toByte())) {
            if (patterns[path[0].code.toByte()]!!.match(path, isDir)) {
                return true
            }
        }
        return otherPatterns.match(path, isDir)
    }
}

class InitialPatterns {
    private val m: MutableMap<Byte, Patterns> = mutableMapOf()

    fun set(initial: Byte, pattern: Pattern) {
        if (m.containsKey(initial)) {
            m[initial]!!.add(pattern)
        } else {
            val patterns = Patterns()
            patterns.add(pattern)
            m[initial] = patterns
        }
    }

    fun get(initial: Byte): Patterns? {
        return m[initial]
    }

    fun size(): Int {
        return m.size
    }
}
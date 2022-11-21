package org.archguard.scanner.cost.ignore

import java.io.File

fun cutN(path: String, n: Int): Pair<String, Boolean> {
    var isLast = true

    var i = 0
    var count = 0
    while (i < path.length - 1) {
        if (path[i] == File.separatorChar) {
            count++
            if (count >= n) {
                isLast = false
                break
            }
        }
        i++
    }
    return Pair(path.substring(0, i + 1), isLast)
}

fun cutLastN(path: String, n: Int): Pair<String, Boolean> {
    var isLast = true
    var i = path.length - 1

    var count = 0
    while (i >= 0) {
        if (path[i] == File.separatorChar) {
            count++
            if (count >= n) {
                isLast = false
                break
            }
        }
        i--
    }
    return Pair(path.substring(i + 1), isLast)
}

fun hasMeta(path: String): Boolean {
    return path.indexOfAny(charArrayOf('*', '?', '[')) >= 0
}
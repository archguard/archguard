package org.archguard.scanner.analyser.ignore

import java.io.File

/**
 * Returns a pair containing the substring of the given path up to the nth occurrence of the file separator character
 * and a boolean value indicating whether the substring is the last part of the path.
 *
 * @param path the path to be cut
 * @param n the number of occurrences of the file separator character to cut the path at
 * @return a pair containing the substring of the path up to the nth occurrence of the file separator character and a boolean value indicating whether the substring is the last part of the path
 */
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

/**
 * Returns a pair containing the last N segments of a given path and a boolean value indicating whether the returned
 * segments are the last ones in the path.
 *
 * @param path the path to be processed
 * @param n the number of segments to be extracted from the end of the path
 * @return a pair where the first element is a string representing the last N segments of the path,
 * and the second element is a boolean value indicating whether the returned segments are the last ones in the path
 */
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
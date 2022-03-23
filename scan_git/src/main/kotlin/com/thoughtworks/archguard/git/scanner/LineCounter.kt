package com.thoughtworks.archguard.git.scanner

import java.io.File
import java.nio.file.Files

val TEN_MB = 10 * 1024 * 1024

class LineCounter {
    companion object {
        fun byPath(path: String): Long {
            return try {
                val file = File(path)
                val size = Files.size(file.toPath())
                if (size < TEN_MB) {
                    Files.lines(file.toPath()).count()
                } else {
                    0
                }
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }
}
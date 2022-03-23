package com.thoughtworks.archguard.git.scanner.model

import java.io.File
import java.nio.file.Files
import kotlin.io.path.isReadable

val TEN_MB = 10 * 1024 * 1024

class LineCounter {
    companion object {
        fun byPath(path: String): Long {
            val file = File(path).toPath()
            if(!file.isReadable()) {
                println("Error path: $path")
                return 0
            }

            return try {
                val size = Files.size(file)
                if (size < TEN_MB) {
                    Files.lines(file).count()
                } else {
                    0
                }
            } catch (e: Exception) {
                println("Error path: $path")
                e.printStackTrace()
                0
            }
        }
    }
}
package com.thoughtworks.archguard.language

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.isReadable


private const val ONE_KB = 1024
private const val TEN_MB = 10 * 1024 * 1024

class LineCounter {
    companion object {
        fun byPath(path: String): Long {
            val file = File(path).toPath()
            if (!file.isReadable()) {
                println("Error path: $path")
                return 0
            }

            return try {
                val size = Files.size(file)
                if (size < TEN_MB) {
                    tryCountLines(file, size)
                } else {
                    0
                }
            } catch (e: Exception) {
                println("Error path: $path")
                e.printStackTrace()
                0
            }
        }

        private fun tryCountLines(file: Path, size: Long): Long {
            var bufferedReader = BufferedReader(InputStreamReader(FileInputStream(file.toFile()), "utf-8"))
            var count = bufferedReader.lines().count()

            val mayBeGbkEncoding = (count == 0L) && (size > ONE_KB)
            if (mayBeGbkEncoding) {
                bufferedReader = BufferedReader(InputStreamReader(FileInputStream(file.toFile()), "GBK"))
                count = bufferedReader.lines().count()
            }

            return count

        }
    }
}
package com.thoughtworks.archguard.scanner.infrastructure

import java.io.File
import java.io.FileOutputStream
import java.net.URL

object FileOperator {

    fun copyTo(file: File, destination: File) {
        file.copyTo(destination, true)
    }

    fun download(url: URL, file: File) {
        url.openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
                output.close()
                input.close()
            }
        }
    }

    fun deleteDirectory(dir: File) {
        if (!dir.exists()) {
            return
        }
        dir.listFiles().orEmpty().forEach {
            if (it.isDirectory) {
                deleteDirectory(it)
            }
            it.delete()
        }
    }
}
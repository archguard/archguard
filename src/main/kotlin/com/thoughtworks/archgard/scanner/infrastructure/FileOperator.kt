package com.thoughtworks.archgard.scanner.infrastructure

import java.io.File
import java.io.FileOutputStream
import java.net.URL

object FileOperator {

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
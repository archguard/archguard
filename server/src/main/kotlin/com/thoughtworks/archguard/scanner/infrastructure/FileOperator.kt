package com.thoughtworks.archguard.scanner.infrastructure

import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object FileOperator {

    fun copyTo(file: File, destination: File) {
        file.copyTo(destination, true)
    }

    fun download(url: URL, file: File) {
        url.openStream().use {
            Files.copy(it, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }

    fun deleteDirectory(dir: File) {
        if (dir.exists()) dir.deleteRecursively()
    }
}

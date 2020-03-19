package com.thoughtworks.archgard.hub.util

import org.springframework.stereotype.Component
import java.io.File

@Component
class FileUtil {

    fun cleanAll(path: String) {
        deleteDirectory(File(path))
    }

    private fun deleteDirectory(dir: File) {
        if (!dir.exists() || !dir.isDirectory) {
            return
        }
        dir.listFiles().orEmpty().forEach {
            if (it.isFile)
                it.delete()
            else if (it.isDirectory)
                deleteDirectory(it)
        }
    }
}

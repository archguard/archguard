package com.thoughtworks.archgard.hub.util

import org.springframework.stereotype.Component
import java.io.File

@Component
class FileUtil {

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

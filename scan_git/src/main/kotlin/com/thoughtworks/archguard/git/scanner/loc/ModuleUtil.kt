package com.thoughtworks.archguard.git.scanner.loc

import java.nio.file.Path

class ModuleUtil {
    companion object {
        fun getModule(path: Path): String? {
            val file = path.toFile()
            var parentFile = file.parentFile
            var i = path.nameCount
            while (!(parentFile.list().orEmpty().toList()).contains("src") || i < 1) {
                parentFile = parentFile.parentFile
                if (parentFile == null) {
                    return null
                }
                i--
            }
            return parentFile.name
        }
    }

}
package com.thoughtworks.archguard.report.domain.module

import java.io.File
import java.nio.file.Path

data class ClassVO(val moduleName: String, val packageName: String, val className: String) {
    companion object {
        fun create(fullName: String): ClassVO {
            val moduleName = fullName.substring(0, fullName.indexOfFirst { it == '.' })
            val packageName = fullName.substring(fullName.indexOfFirst { it == '.' } + 1, fullName.indexOfLast { it == '.' })
            val className = fullName.substring(fullName.indexOfLast { it == '.' } + 1)
            return ClassVO(moduleName, packageName, className)
        }

        fun create(path: Path): ClassVO? {
            if (path.toString().endsWith(".java") || path.toString().endsWith(".kt")) {
                var i = path.nameCount
                var parentFile: File = path.toFile().parentFile

                while (i > 1) {
                    if (path.subpath(i - 1, i).toFile().name == "src")
                        break
                    parentFile = parentFile.parentFile
                    i--
                }
                val moduleName: String = parentFile.toString()
                val packageName: String = path.subpath(i + 3, path.nameCount - 1).toString().replace("/", ".")
                val className = path.subpath(path.nameCount - 1, path.nameCount).toString()
                return ClassVO(moduleName, packageName, className)
            }
            return null;
        }
    }
}
package com.thoughtworks.archguard.report.domain.models

import java.io.File
import java.nio.file.Path

data class ClassVO(val moduleName: String, val packageName: String, val className: String) {
    companion object {
        fun create(fullName: String): ClassVO {
            val moduleName = fullName.substringBefore('.')
            val packageName = fullName.substringAfter('.').substringBeforeLast('.')
            val className = fullName.substringAfterLast('.')
            return ClassVO(moduleName, packageName, className)
        }

        fun create(classFullName: String, moduleName: String): ClassVO {
            val packageName = classFullName.substring(0, classFullName.indexOfLast { it == '.' })
            val className = classFullName.substring(classFullName.indexOfLast { it == '.' } + 1)
            return ClassVO(moduleName, packageName, className)
        }

        fun create(path: Path): ClassVO? {
            if (!isJavaOrKotlinFile(path)) {
                return null
            }

            val moduleName = findModuleName(path)
            val packageName = findPackageName(path)
            val className = findClassName(path)

            return ClassVO(moduleName, packageName, className)
        }

        private fun isJavaOrKotlinFile(path: Path): Boolean {
            val fileName = path.fileName.toString()
            return fileName.endsWith(".java") || fileName.endsWith(".kt")
        }

        private fun findModuleName(path: Path): String {
            var parentFile = path.toFile().parentFile
            var i = path.nameCount

            while (i > 1) {
                if (path.subpath(i - 1, i).toFile().name == "src") {
                    break
                }

                if (parentFile.parentFile == null) {
                    return ""
                }

                parentFile = parentFile.parentFile
                i--
            }

            return parentFile.toString()
        }

        private fun findPackageName(path: Path): String {
            val startIndex = findPackageNameStartIndex(path)
            val endIndex = path.nameCount - 1
            val packageNamePath = path.subpath(startIndex, endIndex)
            return packageNamePath.toString().replace("/", ".")
        }

        private fun findPackageNameStartIndex(path: Path): Int {
            var i = path.nameCount - 1

            while (i > 1) {
                if (path.subpath(i - 1, i).toFile().name == "src") {
                    break
                }

                i--
            }

            return i + 2
        }

        private fun findClassName(path: Path): String {
            val classFullName = path.subpath(path.nameCount - 1, path.nameCount).toString()
            val className = classFullName.substring(0, classFullName.indexOfLast { it == '.' })
            return className
        }
    }
}

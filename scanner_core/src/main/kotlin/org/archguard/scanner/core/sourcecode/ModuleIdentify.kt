package org.archguard.scanner.core.sourcecode

import java.io.File

object ModuleIdentify {
    private const val SEPARATOR = ":"

    // todo: set file cache for all files
    fun lookupModuleName(file: File, base: File): String {
        val srcSplit = file.path.split("src" + File.separator)
        val srcPath = File(srcSplit[0])
        if (isRootModule(srcPath)) {
            return SEPARATOR
        }

        if (file.absolutePath == base.absolutePath) {
            return SEPARATOR
        }

        if (isSubModule(srcPath)) {
            val relativePath = srcPath.relativeTo(base)
            val split = relativePath.path.split(File.separator)
            return SEPARATOR + split.joinToString(SEPARATOR)
        }

        return SEPARATOR
    }

    private val SETTINGS_FILES = arrayOf("settings.gradle", "settings.gradle.kts")

    private fun isGradle(it: File): Boolean {
        return it.name == "build.gradle" || it.name == "build.gradle.kts"
    }

    private fun isMaven(it: File): Boolean {
        return it.name == "pom.xml"
    }

    fun isRootModule(path: File): Boolean {
        if (!path.exists()) {
            return false
        }

        if (!path.isDirectory) {
            return false
        }

        for (buildFileName in SETTINGS_FILES) {
            if (File(path, buildFileName).exists()) {
                return true
            }
        }

        if (File(path, "pom.xml").exists()) {
            return true
        }

        return false
    }

    fun isSubModule(path: File): Boolean {
        if (!path.exists()) {
            return false
        }

        if (!path.isDirectory) {
            return false
        }

        if (containsFiles(File(path, "src"))) {
            return true
        }

        return false
    }

    private fun hasModule(file: File): Boolean {
        return isGradle(file) || isMaven(file)
    }

    private fun containsFiles(sourcePath: File): Boolean {
        val isIncludeSource = sourcePath.exists() && sourcePath.isDirectory
        return if (isIncludeSource) {
            sourcePath.parentFile
                .walk(FileWalkDirection.TOP_DOWN)
                .maxDepth(1)
                .filter {
                    it.isFile && hasModule(it)
                }
                .toList().isNotEmpty()
        } else {
            false
        }
    }
}
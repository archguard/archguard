package com.thoughtworks.archguard.smartscanner.repository

import java.io.File

enum class OS {
    WINDOWS, LINUX, MAC, SOLARIS
}

fun getOS(): OS? {
    val os = System.getProperty("os.name").lowercase()
    return when {
        os.contains("win") -> {
            OS.WINDOWS
        }
        os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
            OS.LINUX
        }
        os.contains("mac") -> {
            OS.MAC
        }
        os.contains("sunos") -> {
            OS.SOLARIS
        }
        else -> null
    }
}

// filePath: point to current file
// sourcePath: like `../../`
//
// output: to normalize path
fun importConvert(filepath: String, importPath: String): String {
    // import "@/src/component/Hello.js"
    val isResolvePath = importPath.startsWith("@/")
    if (isResolvePath) {
        var pathname = importPath.removeRange(0, 2)
        pathname = "src/$pathname"

        if (getOS() == OS.WINDOWS) pathname = pathname.replace("\\", "/")

        return pathname
    }

    if (importPath.startsWith("./") || importPath.startsWith("../")) {
        var file = File(filepath)

        // use parent to convert
        if (file.extension.isNotEmpty()) {
            // src/main.tsx don't have parent
            if (file.parentFile != null) {
                file = file.parentFile
            }
        }

        val resolve = file.resolve(File(importPath))
        return resolve.normalize().toString()
    }

    var finalPath = importPath
    if (getOS() == OS.WINDOWS) finalPath = finalPath.replace("\\", "/")
    return finalPath
}


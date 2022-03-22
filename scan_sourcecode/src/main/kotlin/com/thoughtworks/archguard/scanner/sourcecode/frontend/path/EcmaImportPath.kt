package chapi.app.frontend.path

import java.io.File

fun ecmaImportConvert(workspace: String, filepath: String, importPath: String): String {
    var pathname = filepath
    val isResolvePath = pathname.startsWith("@/")
    if(isResolvePath) {
        pathname = pathname.removeRange(0, 2)
    }

    var relativePath = pathname
    try {
        relativePath = File(pathname).relativeTo(File(workspace)).toString()
    } catch (e: IllegalArgumentException) {
        println(e)
    }

    if (!relativePath.startsWith("./") || !relativePath.startsWith("../")) {
        relativePath = "./$relativePath"
    }

    return importConvert(relativePath, importPath)
}

// filePath: point to current file
// sourcePath: like `../../`
//
// output: to normalize path
fun importConvert(filepath: String, importPath: String): String {
    // import "@/src/component/Hello.js"
    val isResolvePath = importPath.startsWith("@/")
    if(isResolvePath) {
        return importPath.removeRange(0, 2)
    }

    if(importPath.startsWith("./") || importPath.startsWith("../")) {
        var file = File(filepath)

        // use parent to convert
        if(file.extension.isNotEmpty()) {
            // src/main.tsx don't have parent
            if (file.parentFile != null) {
                file = file.parentFile
            }
        }

        val resolve = file.resolve(File(importPath))
        return resolve.normalize().toString()
    }

    return importPath
}

fun relativeRoot(filepath: String, importPath: String): String {
    var pathname = importPath
    val isResolvePath = pathname.startsWith("@/")
    if(isResolvePath) {
        pathname = pathname.removeRange(0, 2)
    }

    var relativePath = pathname
    try {
        relativePath = File(pathname).relativeTo(File(filepath)).toString()
    } catch (e: IllegalArgumentException) {
        println(e)
    }

    return relativePath
}

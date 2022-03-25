package org.archguard.scanner.sourcecode.frontend

fun naming(moduleName: String, nodeName: String): String {
    if (nodeName == "default") {
        return moduleName
    }

    return "${moduleName}::${nodeName}"
}


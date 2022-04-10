package org.archguard.scanner.sourcecode.frontend

import chapi.app.frontend.path.OS
import chapi.app.frontend.path.getOS

fun naming(moduleName: String, nodeName: String): String {
    var moduleName = moduleName
    if (nodeName == "default") {
        return moduleName
    }

    if (getOS() == OS.WINDOWS) moduleName = moduleName.replace("\\", "/")

    return "${moduleName}::${nodeName}"
}


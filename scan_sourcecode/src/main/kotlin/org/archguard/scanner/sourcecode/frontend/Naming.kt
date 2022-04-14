package org.archguard.scanner.sourcecode.frontend

import chapi.app.frontend.path.OS
import chapi.app.frontend.path.getOS

fun naming(moduleName: String, nodeName: String): String {
    var module = moduleName
    if (nodeName == "default") {
        return module
    }

    if (getOS() == OS.WINDOWS) module = module.replace("\\", "/")

    return "${module}::${nodeName}"
}


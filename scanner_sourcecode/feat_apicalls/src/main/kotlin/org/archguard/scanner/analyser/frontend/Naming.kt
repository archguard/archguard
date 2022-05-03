package org.archguard.scanner.analyser.frontend

import org.archguard.scanner.analyser.frontend.path.OS
import org.archguard.scanner.analyser.frontend.path.getOS

fun naming(moduleName: String, nodeName: String): String {
    var module = moduleName
    if (nodeName == "default") {
        return module
    }

    if (getOS() == OS.WINDOWS) module = module.replace("\\", "/")

    return "${module}::${nodeName}"
}


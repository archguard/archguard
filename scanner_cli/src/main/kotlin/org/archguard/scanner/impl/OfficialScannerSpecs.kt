package org.archguard.scanner.impl

import org.archguard.scanner.core.context.ScannerSpec

enum class OfficialScannerSpecs(val spec: ScannerSpec) {
    JAVA(
        ScannerSpec(
            identifier = "java",
            host = "https://github.com/archguard/scanner/releases/download/v1.5.0",
            version = "1.5.0",
            jar = "scan_sourcecode-1.5.0-all.jar",
        )
    )
    ;

    companion object {
        fun specs() = values().map { it.spec }
    }
}

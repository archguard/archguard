package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.AnalyserSpec

enum class OfficialAnalyserSpecs(val spec: AnalyserSpec) {
    JAVA(
        AnalyserSpec(
            identifier = "java",
            host = "https://github.com/archguard/scanner/releases/download/v1.5.0",
            version = "1.5.0",
            jar = "scan_sourcecode-1.5.0-all.jar",
            className = "JavaAnalyser",
        )
    )
    ;

    companion object {
        fun specs() = values().map(OfficialAnalyserSpecs::spec)
    }
}

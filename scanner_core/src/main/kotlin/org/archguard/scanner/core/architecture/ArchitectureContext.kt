package org.archguard.scanner.core.architecture

import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context

interface ArchitectureContext : Context {
    override val type: AnalyserType get() = AnalyserType.ARCHITECTURE

    val path: String
}
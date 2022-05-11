package org.archguard.scanner.core.sca

import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context

interface ScaContext : Context {
    override val type: AnalyserType get() = AnalyserType.SCA

    val path: String
    val language: String
}

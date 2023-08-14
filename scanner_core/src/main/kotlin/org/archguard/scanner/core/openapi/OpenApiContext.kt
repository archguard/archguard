package org.archguard.scanner.core.openapi

import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context

interface OpenApiContext: Context {
    override val type: AnalyserType get() = AnalyserType.OPENAPI
    val path: String
    val branch: String
}


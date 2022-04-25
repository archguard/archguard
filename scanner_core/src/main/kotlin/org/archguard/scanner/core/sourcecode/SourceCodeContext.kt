package org.archguard.scanner.core.sourcecode

import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context

interface SourceCodeContext : Context {
    override val type: AnalyserType get() = AnalyserType.SOURCE_CODE

    val language: String
    val features: List<String>
    val path: String
}

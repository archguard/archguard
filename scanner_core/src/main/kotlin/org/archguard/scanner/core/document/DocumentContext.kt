package org.archguard.scanner.core.document

import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context

interface DocumentContext : Context {
    override val type: AnalyserType get() = AnalyserType.DOCUMENT
    val path: String
    val branch: String
}

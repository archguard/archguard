package org.archguard.scanner.core.estimate

import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context

interface EstimateContext: Context {
    override val type: AnalyserType get() = AnalyserType.ESTIMATE
    val path: String
    val branch: String
}

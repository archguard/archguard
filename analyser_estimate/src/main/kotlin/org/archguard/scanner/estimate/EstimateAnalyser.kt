package org.archguard.scanner.estimate

import org.archguard.scanner.core.estimate.EstimateContext
import org.archguard.scanner.core.estimate.LanguageEstimate


class EstimateAnalyser(val context: EstimateContext) {
    private val service = EstimateService(context)

    fun analyse(): List<LanguageEstimate> {
        return service.analyse()
    }
}

package org.archguard.scanner.estimate

import org.archguard.scanner.core.cost.EstimateContext
import org.archguard.scanner.core.cost.LanguageEstimate


class EstimateAnalyser(val context: EstimateContext) {
    private val service = EstimateService(context)

    fun analyse(): List<LanguageEstimate> {
        return service.analyse()
    }
}

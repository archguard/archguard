package org.archguard.scanner.analyser

import org.archguard.scanner.core.estimate.EstimateContext
import org.archguard.model.LanguageEstimate


class EstimateAnalyser(override val context: EstimateContext) : org.archguard.scanner.core.estimate.EstimateAnalyser {
    private val service = EstimateService(context)

    override fun analyse(): List<LanguageEstimate> {
        val results = service.analyse()
        context.client.saveEstimates(results)
        return results
    }
}

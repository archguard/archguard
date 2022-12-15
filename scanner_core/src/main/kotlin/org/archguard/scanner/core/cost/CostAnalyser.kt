package org.archguard.scanner.core.cost

import org.archguard.scanner.core.Analyser

interface CostAnalyser: Analyser<EstimateContext> {
    fun analyse(): List<LanguageEstimate>
}
package org.archguard.scanner.core.estimate

import org.archguard.scanner.core.Analyser

interface EstimateAnalyser: Analyser<EstimateContext> {
    fun analyse(): List<LanguageEstimate>
}
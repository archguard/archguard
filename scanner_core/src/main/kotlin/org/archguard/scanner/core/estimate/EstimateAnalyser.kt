package org.archguard.scanner.core.estimate

import org.archguard.model.LanguageEstimate
import org.archguard.scanner.core.Analyser

interface EstimateAnalyser: Analyser<EstimateContext> {
    fun analyse(): List<LanguageEstimate>
}
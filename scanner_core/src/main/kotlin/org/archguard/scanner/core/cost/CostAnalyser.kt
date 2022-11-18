package org.archguard.scanner.core.cost

import org.archguard.scanner.core.Analyser

interface CostAnalyser: Analyser<CostContext> {
    fun analyse(): List<EstimateCost>
}
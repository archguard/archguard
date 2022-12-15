package org.archguard.scanner.cost

import org.archguard.scanner.core.cost.CostContext
import org.archguard.scanner.core.cost.LanguageEstimate


class CostAnalyser(val context: CostContext) {
    private val service = CostService(context)

    fun analyse(): List<LanguageEstimate> {
        return service.analyse()
    }
}

//fun main() {
//    CostAnalyser(SimpleCostContext()).analyse()
//}

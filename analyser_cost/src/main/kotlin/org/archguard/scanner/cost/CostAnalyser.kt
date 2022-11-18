package org.archguard.scanner.cost

import org.archguard.scanner.core.cost.CostContext
import org.archguard.scanner.core.cost.EstimateCost

class CostAnalyser(override val context: CostContext) : org.archguard.scanner.core.cost.CostAnalyser {
    private val service = CostService(context)


    override fun analyse(): List<EstimateCost> {
        return service.analyse()
    }
}

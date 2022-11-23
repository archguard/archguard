package org.archguard.scanner.cost

import org.archguard.scanner.core.cost.CostContext
import org.archguard.scanner.core.cost.EstimateCostSummary


class CostAnalyser(val context: CostContext) {
    private val service = CostService(context)

    fun analyse(): List<EstimateCostSummary> {
        return service.analyse()
    }
}
// class SimpleCostContext(
//    override val type: AnalyserType = AnalyserType.COST,
//    override val client: ArchGuardClient = EmptyArchGuardClient(),
//    override val path: String = ".",
//    override val repoId: String = "",
//    override val branch: String = "",
//) : CostContext
//
//fun main() {
//    CostAnalyser(SimpleCostContext()).analyse()
//}

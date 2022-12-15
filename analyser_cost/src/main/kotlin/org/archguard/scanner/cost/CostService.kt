package org.archguard.scanner.cost

import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.cost.CostContext
import org.archguard.scanner.core.cost.LanguageEstimate
import org.archguard.scanner.cost.count.FileWorker
import org.archguard.scanner.cost.estimate.CocomoEstimate
import org.archguard.scanner.cost.estimate.EstimateCost
import java.io.File

class CostService(val context: CostContext) {
    fun analyse(): List<LanguageEstimate> {
        runBlocking {
            val path = File(context.path).canonicalPath.toString()
            val languageSummaries = FileWorker.start(path)

            languageSummaries.map {
                CocomoEstimate().estimate(it.code.toInt())
            }.reduce { acc, estimateCost ->
                EstimateCost(
                    acc.cost + estimateCost.cost,
                    acc.month + estimateCost.month,
                    acc.people + estimateCost.people
                )
            }.let {
                println("total cost: ${it.cost}, month: ${it.month}, people: ${it.people}")
            }
        }

        return emptyList()
    }
}

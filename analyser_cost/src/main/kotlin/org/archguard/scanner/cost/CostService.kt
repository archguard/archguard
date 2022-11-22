package org.archguard.scanner.cost

import CocomoEstimate
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.cost.CostContext
import org.archguard.scanner.core.cost.EstimateCostSummary
import org.archguard.scanner.cost.count.FileWorker
import org.archguard.scanner.cost.estimate.EstimateCost
import java.io.File

class CostService(val context: CostContext) {
    fun analyse(): List<EstimateCostSummary> {
        runBlocking {
            val path = File(context.path).absolutePath.toString()
            val languageSummaries = FileWorker.start(path)
            println(Json.encodeToString(languageSummaries))

            // todo: align the scc
            languageSummaries.map {
                val cost = CocomoEstimate().estimate(it.code.toInt())
                println("${it.name} cost: ${cost.cost}, month: ${cost.month}, people: ${cost.people}")
                cost
            }.reduce { acc, estimateCost ->
                EstimateCost(
                    acc.cost + estimateCost.cost,
                    acc.month + estimateCost.month,
                    acc.people + estimateCost.people
                )
            }.let {
                println("total cost: ${it.cost}, month: ${it.month}, people: ${it.people}")
            }
            ;
        }

        return emptyList()
    }
}

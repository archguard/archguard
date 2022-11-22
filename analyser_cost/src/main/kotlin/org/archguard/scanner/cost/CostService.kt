package org.archguard.scanner.cost

import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.cost.CostContext
import org.archguard.scanner.core.cost.EstimateCost
import org.archguard.scanner.cost.count.FileWorker
import java.io.File

class CostService(val context: CostContext) {
    fun analyse(): List<EstimateCost> {
        runBlocking {
            val path = File(context.path).absolutePath.toString()
            val languageSummaries = FileWorker.start(path)
            println(languageSummaries)
        }

        return emptyList()
    }
}

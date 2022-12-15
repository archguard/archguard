package org.archguard.scanner.cost

import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.cost.CostContext
import org.archguard.scanner.core.cost.LanguageEstimate
import org.archguard.scanner.cost.count.FileWorker
import org.archguard.scanner.cost.estimate.CocomoEstimate
import java.io.File

class CostService(val context: CostContext) {
    fun analyse(): List<LanguageEstimate> {
        return runBlocking {
            val path = File(context.path).canonicalPath.toString()
            val languageSummaries = FileWorker.start(path)

            return@runBlocking languageSummaries.map {
                val estimate = CocomoEstimate().estimate(it.code.toInt())

                LanguageEstimate(
                    cost = estimate.cost.toFloat(),
                    month = estimate.month.toFloat(),
                    people = estimate.people.toFloat(),
                    name = it.name,
                    files = it.files.size.toLong(),
                    lines = it.lines,
                    comment = it.comment,
                    code = it.code,
                    complexity = it.complexity,
                )
            }.toList()
        }
    }
}

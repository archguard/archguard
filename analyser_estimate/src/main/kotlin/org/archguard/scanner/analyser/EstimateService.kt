package org.archguard.scanner.analyser

import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.estimate.EstimateContext
import org.archguard.scanner.core.estimate.LanguageEstimate
import org.archguard.scanner.analyser.count.FileWorker
import org.archguard.scanner.analyser.estimate.CocomoEstimate
import java.io.File

class EstimateService(private val context: EstimateContext) {
    fun analyse(): List<LanguageEstimate> {
        return runBlocking {
            val path = File(context.path).canonicalPath.toString()
            val languageSummaries = FileWorker.start(path)

            return@runBlocking languageSummaries.map {
                val estimate = CocomoEstimate().estimate(it.code.toInt())

                LanguageEstimate(
                    cost = estimate.cost.roundToFloat(),
                    month = estimate.month.roundToFloat(),
                    people = estimate.people.roundToFloat(),
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

// format to 2 decimal places
private fun Double.roundToFloat(): Float {
    // String.format("%.2f", this).toDouble()
    return (this * 100).toInt() / 100.0f
}



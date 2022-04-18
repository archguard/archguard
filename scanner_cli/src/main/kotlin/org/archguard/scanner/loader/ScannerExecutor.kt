package org.archguard.scanner.loader

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.FeatureScanner
import org.archguard.scanner.core.LanguageScanner
import org.archguard.scanner.core.Scanner
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.context.ScannerSpec
import org.archguard.scanner.impl.OfficialScannerSpecs

class ScannerExecutor(
    private val context: Context,
    private val customized: List<ScannerSpec>,
) {
    private val languageScanner = getOrInstall(context.language) as LanguageScanner
    private val featureScanners = runBlocking {
        context.features.map { async { getOrInstall(it) as FeatureScanner<Context> } }.awaitAll()
    }

    // customized is high priority
    private val specs = customized + OfficialScannerSpecs.specs()

    private fun getOrInstall(identifier: String): Scanner<Context> {
        return ScannerLoader.load(context, specs.first { identifier == it.identifier })
    }

    fun execute() {
        val ast = languageScanner.execute()
        // execute with scanner chain
        runBlocking {
            featureScanners.map { async { it.execute(ast) } }.awaitAll()
        }
    }
}

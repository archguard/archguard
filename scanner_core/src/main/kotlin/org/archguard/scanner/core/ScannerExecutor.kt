package org.archguard.scanner.core

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.context.Context
import org.archguard.scanner.context.ScannerSpec
import org.archguard.scanner.loader.ScannerLoader

class ScannerExecutor(
    private val context: Context,
) {
    private val languageScanner = getOrInstall(context.language) as LanguageScanner
    private val featureScanners = runBlocking {
        context.features.map { async { getOrInstall(it) as FeatureScanner<Context> } }.awaitAll()
    }

    private fun getOrInstall(spec: ScannerSpec): Scanner<Context> =
        ScannerLoader.load(context, spec)

    fun execute() {
        val ast = languageScanner.execute()
        runBlocking {
            featureScanners.map { async { it.execute(ast) } }.awaitAll()
        }
    }
}

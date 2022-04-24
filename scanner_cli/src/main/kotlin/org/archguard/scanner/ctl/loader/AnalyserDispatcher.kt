package org.archguard.scanner.ctl.loader

import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.sourcecode.SourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.core.utils.CoroutinesExtension.asyncMap
import org.archguard.scanner.ctl.command.ScannerCommand

class AnalyserDispatcher {
    fun dispatch(command: ScannerCommand) {
        when (command.type) {
            AnalyserType.SOURCE_CODE -> SourceCodeWorker(command.buildSourceCodeContext(), command.getAnalyserSpecs())
            else -> TODO("not implemented yet")
        }.run()
    }
}

private interface AbstractWorker<T : Context> {
    val context: T
    val analyserSpecs: List<AnalyserSpec>
    fun run()

    fun <T> getOrInstall(identifier: String): T {
        val theOne = analyserSpecs.find { identifier == it.identifier }
            ?: throw IllegalArgumentException("No analyser found for identifier: $identifier")
        return AnalyserLoader.load(context, theOne) as T
    }
}

private class SourceCodeWorker(
    override val context: SourceCodeContext,
    override val analyserSpecs: List<AnalyserSpec>,
) : AbstractWorker<SourceCodeContext> {
    override fun run(): Unit = runBlocking {
        val languageAnalyser = getOrInstall<SourceCodeAnalyser>(context.language)
        val ast = languageAnalyser.analyse(null) ?: return@runBlocking
        context.features.asyncMap { getOrInstall<SourceCodeAnalyser>(it).analyse(ast) }
    }
}

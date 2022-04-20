package org.archguard.scanner.ctl.loader

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.sourcecode.SourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs

class AnalyserDispatcher(
    private val context: Context,
    customized: List<AnalyserSpec>,
) {
    private val specs = customized + OfficialAnalyserSpecs.specs()

    private fun getOrInstall(identifier: String): Analyser<Context> {
        // specs 会形成一个优先级队列, 对于相同的identifier, 会优先使用用户自定义的. e.g. 自定义java analyser用来覆盖官方的analyser
        val theOne = specs.find { identifier == it.identifier }
            ?: throw IllegalArgumentException("No analyser found for identifier: $identifier")
        return AnalyserLoader.load(context, theOne)
    }

    fun dispatch() = runBlocking {
        when (context) {
            is SourceCodeContext -> {
                val languageAnalyser = getOrInstall(context.language) as SourceCodeAnalyser
                val ast = languageAnalyser.analyse(null) ?: return@runBlocking
                context.features.map {
                    async {
                        (getOrInstall(it) as SourceCodeAnalyser).analyse(ast)
                    }
                }.awaitAll()
            }
            else -> throw IllegalArgumentException("Unsupported context type")
        }
    }
}

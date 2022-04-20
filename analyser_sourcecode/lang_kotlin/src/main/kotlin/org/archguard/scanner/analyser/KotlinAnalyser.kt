package org.archguard.scanner.analyser

import chapi.ast.kotlinast.AnalysisMode
import chapi.domain.core.CodeDataStruct
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import java.io.File

class KotlinAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    private val client = context.client
    private val impl = chapi.ast.kotlinast.KotlinAnalyser()

    override fun analyse(): List<CodeDataStruct> = runBlocking {
        getFilesByPath(context.path) {
            it.absolutePath.endsWith(".kt") || it.absolutePath.endsWith(".kts")
        }
            .map { async { analysisByFile(it) } }.awaitAll()
            .flatten()
            .also { client.saveDataStructure(it, context.systemId, context.language) }
    }

    private fun analysisByFile(file: File): List<CodeDataStruct> {
        fun postProcess(it: CodeDataStruct): CodeDataStruct = it.apply { it.FilePath = file.absolutePath }

        val codeContainer = impl.analysis(file.readContent(), file.name, AnalysisMode.Full)

        return codeContainer.DataStructures.map(::postProcess)
    }
}

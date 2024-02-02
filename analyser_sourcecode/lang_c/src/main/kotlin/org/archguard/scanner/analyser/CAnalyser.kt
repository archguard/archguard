package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import java.io.File

class CAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    private val client = context.client
    private val analyser = chapi.ast.cast.CAnalyser()
    private val logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

    override fun analyse(): List<CodeDataStruct> = runBlocking {
        getFilesByPath(context.path) {
            it.absolutePath.endsWith(".h")
        }.map {
            async { analyser.addSource(it.readContent()) }
        }

        getFilesByPath(context.path)
            .filter { it.absolutePath.endsWith(".c") }
            .map { async { analysisByFile(it) } }.awaitAll()
            .flatten()
            .also { client.saveDataStructure(it) }
    }

    private fun analysisByFile(file: File): List<CodeDataStruct> {
        logger.info("analysis file: ${file.absolutePath}")
        val content = file.readContent()
        val lines = content.lines()
        val codeContainer = analyser.analysis(content, file.name)

        return codeContainer.DataStructures.map { ds ->
            ds.apply {
                ds.Imports = codeContainer.Imports
                ds.FilePath = file.absolutePath

                if (context.withFunctionCode) {
                    ds.Content = contentByPosition(lines, ds.Position)
                    ds.Functions.map { it.apply { it.Content = contentByPosition(lines, it.Position) } }
                }
            }
        }
    }
}

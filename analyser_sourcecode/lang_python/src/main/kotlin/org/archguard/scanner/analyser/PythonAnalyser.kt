package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import java.io.File

class PythonAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    private val client = context.client
    private val impl = chapi.ast.pythonast.PythonAnalyser()

    override fun analyse(): List<CodeDataStruct> = runBlocking {
        getFilesByPath(context.path) {
            it.absolutePath.endsWith(".py")
        }
            .map { async { analysisByFile(it) } }.awaitAll()
            .flatten()
            .also { client.saveDataStructure(it) }
    }

    private fun analysisByFile(file: File): List<CodeDataStruct> {
        val content = file.readContent()
        val lines = content.lines()
        val codeContainer = impl.analysis(content, file.name)

        return codeContainer.DataStructures.map { ds ->
            ds.apply {
                ds.Imports = codeContainer.Imports
                ds.FilePath = file.absolutePath

                if (context.withFunctionCode) {
                    ds.Functions.map { it.apply { it.Content = contentByPosition(lines, it.Position) } }
                }
            }
        }
    }
}

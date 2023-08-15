package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import java.io.File

class CSharpAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    private val client = context.client
    private val impl = chapi.ast.csharpast.CSharpAnalyser()

    override fun analyse(): List<CodeDataStruct> = runBlocking {
        val basepath = File(context.path)

        getFilesByPath(context.path) {
            it.absolutePath.endsWith(".cs")
        }
            .map { async { analysisByFile(it, basepath) } }.awaitAll()
            .flatten()
            .also { client.saveDataStructure(it) }
    }

    private fun analysisByFile(file: File, basepath: File): List<CodeDataStruct> {
        val content = file.readContent()
        val lines = content.lines()
        val codeContainer = impl.analysis(content, file.name)

        return codeContainer.Containers.flatMap { container ->
            container.DataStructures.map { ds ->
                ds.apply {
                    ds.Imports = codeContainer.Imports
                    ds.FilePath = file.relativeTo(basepath).toString()

                    if (context.withFunctionCode) {
                        ds.Functions.map { ds.apply { ds.Content = contentByPosition(lines, ds.Position) } }
                    }
                }
            }
        }
    }
}

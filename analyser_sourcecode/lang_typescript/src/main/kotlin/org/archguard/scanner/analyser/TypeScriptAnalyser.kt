package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.DataStructType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import java.io.File

class TypeScriptAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    private val client = context.client
    private val impl = chapi.ast.typescriptast.TypeScriptAnalyser()

    // TODO put into context or as an additional parameter
    private val ignoreMinFile = true

    override fun analyse(): List<CodeDataStruct> = runBlocking {
        val basepath = File(context.path)

        getFilesByPath(context.path) {
            val path = it.absolutePath

            val isNormalFile = path.endsWith(".ts") ||
                path.endsWith(".tsx") ||
                path.endsWith(".js") ||
                path.endsWith(".jsx")
            val isNotMinFile = path.endsWith(".min.js")

            if (ignoreMinFile) isNormalFile && !isNotMinFile
            else isNormalFile
        }
            .map { async {
                analysisByFile(it, basepath) } }.awaitAll()
            .flatten()
            .also { client.saveDataStructure(it) }
    }

    private fun analysisByFile(file: File, basepath: File): List<CodeDataStruct> {
        val workspace = File(context.path)
        val codeContainer = impl.analysis(file.readContent(), file.toRelativeString(workspace))
        return codeContainer.DataStructures.map {
            it.apply {
                if (it.Type != DataStructType.INTERFACE) {
                    it.Imports = codeContainer.Imports
                    it.FilePath = file.relativeTo(basepath).toString()
                }
            }
        }
    }
}

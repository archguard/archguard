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
    private val impl = chapi.ast.typescriptast.TypeScriptAnalyser()
    private val logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

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

            if (ignoreMinFile) isNormalFile && !isNotMinFile else isNormalFile
        }
            .map {
                async {
                    println(it.absolutePath)
                    analysisByFile(it, basepath)
                }
            }.awaitAll()
            .flatten()
    }

    private fun analysisByFile(file: File, basepath: File): List<CodeDataStruct> {
        logger.info("analysis file: ${file.absolutePath}")

        val workspace = File(context.path)
        val content = file.readContent()
        val lines = content.lines()
        val codeContainer = impl.analysis(content, file.toRelativeString(workspace))

        return codeContainer.DataStructures.map { ds ->
            ds.apply {
                if (ds.Type != DataStructType.INTERFACE) {
                    ds.Imports = codeContainer.Imports
                    ds.FilePath = file.relativeTo(basepath).toString()
                }

                if (context.withFunctionCode) {
                    ds.Content = contentByPosition(lines, ds.Position)
                    ds.Functions.map { it.apply { it.Content = contentByPosition(lines, it.Position) } }
                }
            }
        }
    }
}

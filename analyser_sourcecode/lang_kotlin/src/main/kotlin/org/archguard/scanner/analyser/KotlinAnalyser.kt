package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import chapi.parser.ParseMode
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.ModuleIdentify
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import java.io.File

class KotlinAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    private val impl = chapi.ast.kotlinast.KotlinAnalyser()

    private val logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

    override fun analyse(): List<CodeDataStruct> = runBlocking {
        val basepath = File(context.path)
        getFilesByPath(context.path) {
            it.absolutePath.endsWith(".kt") || it.absolutePath.endsWith(".kts")
        }
            .map { async { analysisByFile(it, basepath) } }.awaitAll()
            .flatten()
    }

    private fun analysisByFile(file: File, basepath: File): List<CodeDataStruct> {
        logger.info("analysis file: ${file.absolutePath}")

        val content = file.readContent()
        val lines = content.lines()
        val moduleName = ModuleIdentify.lookupModuleName(file, basepath)
        val codeContainer = impl.analysis(content, file.name, ParseMode.Full)

        return codeContainer.DataStructures.map { ds ->
            ds.apply {
                ds.Module = moduleName
                ds.FilePath = file.relativeTo(basepath).toString()

                if (context.withFunctionCode) {
                    ds.Content = contentByPosition(lines, ds.Position)
                    ds.Functions.map { it.apply { it.Content = contentByPosition(lines, it.Position) } }
                }
            }
        }
    }
}

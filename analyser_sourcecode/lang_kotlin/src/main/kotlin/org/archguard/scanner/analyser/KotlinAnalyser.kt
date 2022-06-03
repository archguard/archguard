package org.archguard.scanner.analyser

import chapi.ast.kotlinast.AnalysisMode
import chapi.domain.core.CodeDataStruct
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.ModuleIdentify
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import java.io.File

class KotlinAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    private val client = context.client
    private val impl = chapi.ast.kotlinast.KotlinAnalyser()

    override fun analyse(): List<CodeDataStruct> = runBlocking {
        val basepath = File(context.path)
        getFilesByPath(context.path) {
            it.absolutePath.endsWith(".kt") || it.absolutePath.endsWith(".kts")
        }
            .map { async { analysisByFile(it, basepath) } }.awaitAll()
            .flatten()
            .also { client.saveDataStructure(it) }
    }

    private fun analysisByFile(file: File, basepath: File): List<CodeDataStruct> {
        val moduleName = ModuleIdentify.lookupModuleName(file, basepath)
        return impl.analysis(file.readContent(), file.name, AnalysisMode.Full).DataStructures
            .map {
                it.apply {
                    it.Module = moduleName
                    it.FilePath = file.relativeTo(basepath).toString()
                }
            }
    }
}

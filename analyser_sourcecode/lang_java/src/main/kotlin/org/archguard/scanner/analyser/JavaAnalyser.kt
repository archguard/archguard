package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.core.utils.CoroutinesExtension.asyncMap
import java.io.File

class JavaAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    private val client = context.client
    private val impl = chapi.ast.javaast.JavaAnalyser()

    private lateinit var basicNodes: Array<CodeDataStruct>
    private lateinit var classes: Array<String>

    override fun analyse(): List<CodeDataStruct> = runBlocking {
        val files = getFilesByPath(context.path) {
            it.absolutePath.endsWith(".java")
        }
        basicNodes = files.asyncMap { analysisBasicInfoByFile(it) }.flatten().toTypedArray()
        classes = basicNodes.map { it.getClassFullName() }.toTypedArray()

        files.asyncMap { analysisFullInfoByFile(it) }.flatten().toList()
    }

    private fun analysisBasicInfoByFile(file: File): List<CodeDataStruct> {
        val codeContainer = impl.identBasicInfo(file.readContent(), file.name)
        return codeContainer.DataStructures.map { ds -> ds.apply { ds.Imports = codeContainer.Imports } }
    }

    private fun analysisFullInfoByFile(file: File): List<CodeDataStruct> {
        val codeContainer = impl.identFullInfo(file.readContent(), file.name, classes, basicNodes)
        return codeContainer.DataStructures.map { ds -> ds.apply { ds.Imports = codeContainer.Imports } }
    }
}

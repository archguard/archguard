package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import java.io.File

class MyLanguageAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    override fun analyse(): List<CodeDataStruct> {
        return File(context.path).walk()
            .filter { it.isFile && it.name.endsWith("Test.class") }
            .map {
                println(it.name)
                println(it.absolutePath)
                CodeDataStruct(NodeName = it.name, FilePath = it.absolutePath)
            }
            .toList()
    }
}

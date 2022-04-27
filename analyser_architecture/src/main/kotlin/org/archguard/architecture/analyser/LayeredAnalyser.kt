package org.archguard.architecture.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.ASTSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext

data class LayeredContext(
    override val language: String = "",
    override val features: List<String> = listOf(),
    override val client: ArchGuardClient = LayeredClient(),
    override val path: String = "",
    val systemId: String = "",
) : SourceCodeContext

class LayeredAnalyser(override val context: LayeredContext) : ASTSourceCodeAnalyser {
    override fun analyse(input: List<CodeDataStruct>): Any? {
        input.forEach {
            // todo: from package name
        }

        return null
    }
}
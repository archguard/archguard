package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.dto.ContainerService
import org.archguard.scanner.core.sourcecode.ASTSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext

class MyApiAnalyser(override val context: SourceCodeContext) : ASTSourceCodeAnalyser {
    private val client = context.client

    override fun analyse(input: List<CodeDataStruct>): Any? {
        // process the ast
        val apis = input
            .filter { it.NodeName.contains("API") }
            .map { ContainerService(name = it.NodeName) }

        // persist the data
        client.saveApi(apis, context.systemId, context.language)

        return null
    }
}

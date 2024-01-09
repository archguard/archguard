package org.archguard.architecture

import org.archguard.architecture.core.Workspace
import org.archguard.scanner.analyser.ApiCallAnalyser
import org.archguard.scanner.analyser.JavaAnalyser
import org.archguard.scanner.analyser.ScaAnalyser
import org.archguard.scanner.core.architecture.ArchitectureContext
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.EmptyArchGuardClient
import org.archguard.scanner.core.sca.ScaContext
import org.archguard.scanner.core.sourcecode.SourceCodeContext

class ArchitectureAnalyser(override val context: ArchitectureContext) :
    org.archguard.scanner.core.architecture.ArchitectureAnalyser {
    override fun analyse(): List<ArchitectureView> {
        // TODO: Implement this method
        val sourceCodeContext = ArchSourceCodeContext(language = "java", path = context.path)
        val dataStructs = JavaAnalyser(sourceCodeContext)
            .analyse()
        val projectDependencies = ScaAnalyser(ArchScaContext(path = context.path, language = "java")).analyse()
        val services = ApiCallAnalyser(sourceCodeContext).analyse(dataStructs)

        val workspace = Workspace(
            dataStructs,
            service = services
        ).analysis()

        return listOf()
    }
}

data class ArchSourceCodeContext(
    override val language: String,
    override val features: List<String> = listOf(),
    override val client: ArchGuardClient = EmptyArchGuardClient(),
    override val path: String,
    override val withFunctionCode: Boolean = true,
    override val debug: Boolean = false,
) : SourceCodeContext

data class ArchScaContext(
    override val path: String,
    override val client: ArchGuardClient = EmptyArchGuardClient(),
    override val language: String,
) : ScaContext

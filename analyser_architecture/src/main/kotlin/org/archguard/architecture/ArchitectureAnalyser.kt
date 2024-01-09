package org.archguard.architecture

import org.archguard.architecture.core.Workspace
import org.archguard.scanner.analyser.ApiCallAnalyser
import org.archguard.scanner.analyser.EstimateAnalyser
import org.archguard.scanner.analyser.JavaAnalyser
import org.archguard.scanner.analyser.ScaAnalyser
import org.archguard.scanner.core.architecture.ArchitectureContext
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.EmptyArchGuardClient
import org.archguard.scanner.core.estimate.EstimateContext
import org.archguard.scanner.core.sca.ScaContext
import org.archguard.scanner.core.sourcecode.SourceCodeContext

data class SampleArchitectureContext(
    override val path: String,
    override val client: ArchGuardClient = EmptyArchGuardClient(),
) : ArchitectureContext

class ArchitectureAnalyser(override val context: ArchitectureContext) :
    org.archguard.scanner.core.architecture.ArchitectureAnalyser {
    override fun analyse(): List<ArchitectureView> {
        val sourceCodeContext = ArchSourceCodeContext(language = "Java", path = context.path)
        val dataStructs = JavaAnalyser(sourceCodeContext)
            .analyse()
        val projectDependencies = ScaAnalyser(ArchScaContext(path = context.path, language = "java"))
            .analysisByPackages()

        val services = ApiCallAnalyser(sourceCodeContext).analyse(dataStructs)

        val languageEstimates = EstimateAnalyser(ArchEstimateContext(context.path)).analyse()

        val workspace = Workspace(
            dataStructs,
            projectDependencies,
            service = services
        ).analysis()
        workspace.languageSummary = languageEstimates

        return listOf(workspace)
    }
}

data class ArchEstimateContext(
    override val path: String,
    override val branch: String = "",
    override val client: ArchGuardClient = EmptyArchGuardClient(),
) : EstimateContext

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

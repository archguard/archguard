package org.archguard.architecture

import org.archguard.architecture.core.Workspace
import org.archguard.scanner.analyser.*
import org.archguard.scanner.core.architecture.ArchitectureContext
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.EmptyArchGuardClient
import org.archguard.scanner.core.estimate.EstimateContext
import org.archguard.scanner.core.sca.ScaContext
import org.archguard.scanner.core.sourcecode.SourceCodeContext

class ArchitectureAnalyser(override val context: ArchitectureContext) :
    org.archguard.scanner.core.architecture.ArchitectureAnalyser {
    override fun analyse(language: String): List<ArchitectureView> {
        val sourceCodeContext = ArchSourceCodeContext(language = language, path = context.path)

        /// try to add by different languages
        val dataStructs = when (language) {
            "java" -> {
                JavaAnalyser(sourceCodeContext).analyse()
            }
            "kotlin" -> {
                KotlinAnalyser(sourceCodeContext).analyse()
            }
            "go" -> {
                GoAnalyser(sourceCodeContext).analyse()
            }
            else -> {
                throw IllegalArgumentException("Unsupported language: $language")
            }
        }

        val projectDependencies = ScaAnalyser(ArchScaContext(path = context.path, language = language))
            .analysisByPackages()

        val services = ApiCallAnalyser(sourceCodeContext).analyse(dataStructs)

        val languageEstimates = EstimateAnalyser(ArchEstimateContext(context.path)).analyse()

        val workspace = Workspace(
            dataStructs,
            projectDependencies,
            service = services
        ).analysis()
        workspace.physicalStructure.languageEstimate = languageEstimates

        return listOf(workspace)
    }
}

val archGuardClient = EmptyArchGuardClient()

data class SampleArchitectureContext(
    override val path: String,
    override val client: ArchGuardClient = archGuardClient,
) : ArchitectureContext

data class ArchEstimateContext(
    override val path: String,
    override val branch: String = "",
    override val client: ArchGuardClient = archGuardClient,
) : EstimateContext

data class ArchSourceCodeContext(
    override val language: String,
    override val features: List<String> = listOf(),
    override val client: ArchGuardClient = archGuardClient,
    override val path: String,
    override val withFunctionCode: Boolean = true,
    override val debug: Boolean = false,
) : SourceCodeContext

data class ArchScaContext(
    override val path: String,
    override val client: ArchGuardClient = archGuardClient,
    override val language: String,
) : ScaContext

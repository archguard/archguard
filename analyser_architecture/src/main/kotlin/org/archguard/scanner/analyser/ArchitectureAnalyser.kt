package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.archguard.architecture.ArchitectureView
import org.archguard.scanner.core.architecture.ArchitectureContext
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.EmptyArchGuardClient
import org.archguard.scanner.core.estimate.EstimateContext
import org.archguard.scanner.core.sca.ScaContext
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.slf4j.LoggerFactory
import java.io.File

class ArchitectureAnalyser(override val context: ArchitectureContext) :
    org.archguard.scanner.core.architecture.ArchitectureAnalyser {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun analyse(): List<ArchitectureView> {
        val sourceCodeContext = ArchSourceCodeContext(language = context.language, path = context.path)

        logger.info("start analysis architecture ---- ${context.language}")
        /// if
        var dataStructs: MutableList<CodeDataStruct> = mutableListOf()
        if (context.withStructureCache) {
            ///  load and serialize the dataStructs from 0_code.json
            val file = File("0_code.json")
            if (file.exists()) {
                dataStructs = Json.decodeFromString(file.readText())
            } else {
                dataStructs = analysisBasedSourceCode(sourceCodeContext)
            }
        } else {
            dataStructs = analysisBasedSourceCode(sourceCodeContext)
        }

        logger.info("start analysis proto ---- ${context.language}")
        dataStructs += ProtoAnalyser(sourceCodeContext).analyse()

        logger.info("start analysis sca ---- ${context.language}")
        val projectDependencies = ScaAnalyser(ArchScaContext(path = context.path, language = context.language))
            .analysisByPackages()

        logger.info("start analysis api ---- ${context.language}")
        val services = ApiCallAnalyser(sourceCodeContext).analyse(dataStructs)

        logger.info("start analysis estimate ---- ${context.language}")
        val languageEstimates = EstimateAnalyser(ArchEstimateContext(context.path)).analyse()

        logger.info("start analysis database relation ---- ${context.language}")
        val databaseRelations = DataMapAnalyser(sourceCodeContext).analyse(dataStructs)

        logger.info("start analysis workspace ---- ${context.language}")
        val architectureView: ArchitectureView = WorkspaceAnalyser(
            dataStructs,
            projectDependencies,
            service = services,
            databaseRelations = databaseRelations,
            language = context.language
        ).analysis(workspace = context.path)

        architectureView.physicalStructure.languageEstimate = languageEstimates

        logger.info("finish analysis architecture ---- ${context.language}")
        context.client.saveArchitecture(listOf(architectureView))
        logger.info("save architecture ---- ${context.language}")
        return listOf(architectureView)
    }

    private fun analysisBasedSourceCode(sourceCodeContext: ArchSourceCodeContext) =
        when (context.language) {
            "java" -> {
                JavaAnalyser(sourceCodeContext).analyse()
            }

            "kotlin" -> {
                KotlinAnalyser(sourceCodeContext).analyse()
            }

            "go", "golang" -> {
                GoAnalyser(sourceCodeContext).analyse()
            }

            else -> {
                throw IllegalArgumentException("Unsupported language: $context.language")
            }
        }.toMutableList()
}

val archGuardClient = EmptyArchGuardClient()

data class CliArchitectureAnalysisContext(
    override val path: String,
    override val client: ArchGuardClient = archGuardClient,
    override val language: String,
    override val withStructureCache: Boolean = false
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

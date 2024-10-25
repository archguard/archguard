package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.analyser.backend.CSharpApiAnalyser
import org.archguard.scanner.analyser.backend.GoApiAnalyser
import org.archguard.scanner.analyser.backend.JavaCompositeApiAnalyser
import org.archguard.scanner.analyser.backend.ProtobufApiAnalyser
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.scanner.analyser.frontend.FrontendApiAnalyser
import org.archguard.scanner.core.sourcecode.ASTSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.ContainerService
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.slf4j.LoggerFactory

class ApiCallAnalyser(override val context: SourceCodeContext) : ASTSourceCodeAnalyser {
    private val client = context.client
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun analyse(input: List<CodeDataStruct>): List<ContainerService> {
        val language = context.language.lowercase()
        val path = context.path

        logger.info("start analysis api ---- $language")

        val analyser: ApiAnalyser? = when (language) {
            "typescript", "javascript" -> {
                val feApiAnalyser = FrontendApiAnalyser(listOf())
                input.forEach { data ->
                    feApiAnalyser.analysisByNode(data, path)
                }

                feApiAnalyser
            }

            "c#", "csharp" -> {
                val csharpApiAnalyser = CSharpApiAnalyser()
                input.forEach { data ->
                    csharpApiAnalyser.analysisByNode(data, "")
                }

                csharpApiAnalyser
            }

            "java", "kotlin" -> {
                val apiAnalyser = JavaCompositeApiAnalyser()
                input.forEach { data ->
                    apiAnalyser.analysisByNode(data, "")
                }

                apiAnalyser
            }

            "golang" -> {
                val apiAnalyser = GoApiAnalyser()
                input.forEach { data ->
                    apiAnalyser.analysisByNode(data, "")
                }

                apiAnalyser
            }

            else -> null
        }

        val apiCalls = analyser?.toContainerServices() ?: listOf()

        // get all input and filter `.proto` files to analysis
        val protobufs = input.filter { it.FilePath.endsWith(".proto") }
        val protobufApiAnalyser = ProtobufApiAnalyser()
        protobufs.forEach { data ->
            protobufApiAnalyser.analysisByNode(data, path)
        }

        val services = protobufApiAnalyser.toContainerServices()

        val results = (apiCalls + services).mapNotNull {
            if (it.resources.isEmpty() && it.demands.isEmpty()) null else it
        }
        client.saveApi(results)
        return results
    }
}

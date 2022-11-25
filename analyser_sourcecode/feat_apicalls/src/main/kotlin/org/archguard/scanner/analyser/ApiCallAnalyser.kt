package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.analyser.backend.CSharpApiAnalyser
import org.archguard.scanner.analyser.backend.GoApiAnalyser
import org.archguard.scanner.analyser.backend.JavaApiAnalyser
import org.archguard.scanner.core.sourcecode.ASTSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.ContainerService
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.sourcecode.frontend.FrontendApiAnalyser
import org.slf4j.LoggerFactory

class ApiCallAnalyser(override val context: SourceCodeContext) : ASTSourceCodeAnalyser {
    private val client = context.client
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun analyse(input: List<CodeDataStruct>): List<ContainerService> {
        val language = context.language.lowercase()
        val path = context.path

        val apiCalls = when (language) {
            "typescript", "javascript" -> {
                logger.info("start analysis frontend api ---- $language")

                val feApiAnalyser = FrontendApiAnalyser()
                input.forEach { data ->
                    feApiAnalyser.analysisByNode(data, path)
                }

                feApiAnalyser.toContainerServices()
            }

            "c#", "csharp" -> {
                logger.info("start analysis backend api ---- CSharp")

                val csharpApiAnalyser = CSharpApiAnalyser()
                input.forEach { data ->
                    csharpApiAnalyser.analysisByNode(data, "")
                }

                csharpApiAnalyser.toContainerServices()
            }

            "java", "kotlin" -> {
                logger.info("start analysis backend api ---- $language")

                val apiAnalyser = JavaApiAnalyser()
                input.forEach { data ->
                    apiAnalyser.analysisByNode(data, "")
                }

                apiAnalyser.toContainerServices()
            }

            "go" -> {
                logger.info("start analysis backend api ---- $language")

                val apiAnalyser = GoApiAnalyser()
                input.forEach { data ->
                    apiAnalyser.analysisByNode(data, "")
                }

                apiAnalyser.toContainerServices()
            }

            else -> throw IllegalArgumentException("Unsupported language: $language")
        }

        client.saveApi(apiCalls)
        return apiCalls
    }
}

package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.analyser.backend.CSharpApiAnalyser
import org.archguard.scanner.common.backend.JavaApiAnalyser
import org.archguard.scanner.core.sourcecode.ASTSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.sourcecode.frontend.FrontendApiAnalyser
import org.slf4j.LoggerFactory
import java.io.File

class ApiCallAnalyser(override val context: SourceCodeContext) : ASTSourceCodeAnalyser {
    private val client = context.client
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun analyse(input: List<CodeDataStruct>): Any? {
        val language = context.language.lowercase()
        val path = context.path

        val apiCalls = when (language) {
            "typescript", "javascript" -> {
                logger.info("start analysis frontend api ---- $language")

                val feApiAnalyser = FrontendApiAnalyser()
                // save class first, and can query dependencies for later
                val absPath = File(path).absolutePath

                input.forEach { data ->
                    feApiAnalyser.analysisByNode(data, absPath)
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
            else -> throw IllegalArgumentException("Unsupported language: $language")
        }

        File("apis.json").writeText(Json.encodeToString(apiCalls))
        client.saveApi(apiCalls.toList())
        return apiCalls
    }
}

package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private data class ApiModel(
    var CodeInfo: String = "",
    var SourceNode: String,
    var HttpMethod: String,
    var BaseUri: String,
    var RouteUri: String,
    var Request: String = "",
    var ResponseModel: String = ""
)

internal class CSharpAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveDataStructure(any()) } just runs
    }
    private val mockContext = mockk<SourceCodeContext> {
        every { client } returns mockClient
        every { withFunctionCode } returns false
    }

    @Nested
    inner class E2E {
        @Test
        fun shouldCountModelSize() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("e2e").path
            val nodes = CSharpAnalyser(mockContext).analyse()
            assertEquals(5, nodes.size)
        }

        @Test
        fun shouldIdentifySamePackage() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("e2e").path
            val nodes = CSharpAnalyser(mockContext).analyse()
            assertEquals(5, nodes.size)

            val models = mutableListOf<ApiModel>()

            nodes.map { node ->
                val routeAnnotation = node.filterAnnotations("RoutePrefix", "Route")
                if (routeAnnotation.isNotEmpty()) {
                    val baseUrl = routeAnnotation[0].KeyValues[0].Value
                    node.Functions.forEach { createFunctionApi(it, baseUrl, models, node) }
                }
            }

            models.forEach {
                println("| ${it.SourceNode} | ${it.HttpMethod} | /${it.BaseUri}/${it.RouteUri} | ${it.Request} | ${it.ResponseModel}  |")
            }

            assertEquals(0, models.size);
        }

        private fun createFunctionApi(
            func: CodeFunction,
            baseUrl: String,
            models: MutableList<ApiModel>,
            node: CodeDataStruct
        ) {
            var httpMethod = "";
            var route = "";
            for (annotation in func.Annotations) {
                when (annotation.Name) {
                    "HttpGet" -> httpMethod = "Get    "
                    "HttpPost" -> httpMethod = "Post   "
                    "HttpDelete" -> httpMethod = "Delete "
                    "HttpPut" -> httpMethod = "Put    "
                }
                if (annotation.Name == "Route") {
                    route = annotation.KeyValues[0].Value
                }
            }

            if (route.isNotEmpty() && httpMethod.isNotEmpty()) {
                val params = func.Parameters.map {
                    it.TypeType + ":" + it.TypeValue
                }.toList().joinToString(",")

                models += ApiModel(
                    SourceNode = node.NodeName,
                    BaseUri = baseUrl,
                    HttpMethod = httpMethod,
                    RouteUri = route,
                    Request = params
                )
            }
        }
    }
}

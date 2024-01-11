package org.archguard.scanner.analyser.backend;

import chapi.domain.core.AnnotationKeyValue
import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.scanner.core.sourcecode.ContainerSupply
import org.archguard.scanner.core.sourcecode.ContainerService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CSharpApiAnalyserTest {
    @Test
    fun `given node with HttpPost annotation, when analysisByNode is called, then resources should be created with correct values`() {
        // given
        val node = CodeDataStruct(
            NodeName = "TestController",
            Annotations = listOf(
                CodeAnnotation(
                    Name = "Route",
                    KeyValues = listOf(
                        AnnotationKeyValue(
                            Key = "value",
                            Value = "api/test"
                        )
                    )
                )
            ),
            Functions = listOf(
                CodeFunction(
                    Annotations = listOf(
                        CodeAnnotation(
                            Name = "HttpPost"
                        ),
                        CodeAnnotation(
                            Name = "Route",
                            KeyValues = listOf(
                                AnnotationKeyValue(
                                    Key = "value",
                                    Value = "users"
                                )
                            )
                        )
                    )
                )
            )
        )
        val workspace = "/path/to/workspace"
        val analyser = CSharpApiAnalyser()

        // when
        analyser.analysisByNode(node, workspace)

        // then
        val expectedResources = listOf(
            ContainerSupply(
                sourceUrl = "/api/test/users",
                sourceHttpMethod = "Post",
                packageName = "",
                className = "TestController",
                methodName = ""
            )
        )

        assertEquals(expectedResources, analyser.resources)
    }

    @Test
    fun `given node without route and http method annotations, when analysisByNode is called, then resources should not be created`() {
        // given
        val node = CodeDataStruct()
        node.NodeName = "TestController"
        val workspace = "/path/to/workspace"
        val analyser = CSharpApiAnalyser()

        // when
        analyser.analysisByNode(node, workspace)

        // then
        val expectedResources = emptyList<ContainerSupply>()
        assertEquals(expectedResources, analyser.resources)
    }

    @Test
    fun `given resources, when toContainerServices is called, then a list of ContainerService should be returned`() {
        // given
        val analyser = CSharpApiAnalyser()
        analyser.resources = listOf(
            ContainerSupply(
                sourceUrl = "/users",
                sourceHttpMethod = "Get",
                packageName = "",
                className = "UserController",
                methodName = ""
            ),
            ContainerSupply(
                sourceUrl = "/users",
                sourceHttpMethod = "Post",
                packageName = "",
                className = "UserController",
                methodName = ""
            )
        )

        // when
        val containerServices = analyser.toContainerServices()

        // then
        val expectedContainerServices = listOf(
            ContainerService(name = "", resources = analyser.resources)
        )
        assertEquals(expectedContainerServices, containerServices)
    }
}

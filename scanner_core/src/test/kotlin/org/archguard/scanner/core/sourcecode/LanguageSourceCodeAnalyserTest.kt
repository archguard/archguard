package org.archguard.scanner.core.sourcecode;

import chapi.domain.core.*
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.EmptyArchGuardClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LanguageSourceCodeAnalyserTest {
    val analyser = object : LanguageSourceCodeAnalyser {
        override fun analyse(): List<CodeDataStruct> {
            return listOf()
        }

        override val context: SourceCodeContext
            get() = object : SourceCodeContext {
                override val language: String = "language"
                override val features: List<String> = listOf("feature1", "feature2")
                override val path: String = "path"
                override val withFunctionCode: Boolean = true
                override val debug: Boolean = true
                override val client: ArchGuardClient = EmptyArchGuardClient()
            }
    }

    @Test
    fun should_return_displayed_function_when_display_called() {
        // Given
        val analyser = analyser

        val function = CodeFunction(
            Name = "function_name",
            Parameters = listOf(
                CodeProperty(TypeValue = "param1", TypeType = "type1"),
                CodeProperty(TypeValue = "param2", TypeType = "type2")
            ),
            ReturnType = "return_TYPE",
            Annotations = listOf(
                CodeAnnotation(
                    Name = "annotation",
                    KeyValues = listOf(
                        AnnotationKeyValue(Key = "key1", Value = "value1"),
                        AnnotationKeyValue(Key = "key2", Value = "value2")
                    )
                )
            ),
            FunctionCalls = listOf(
                CodeCall(
                    Package = "package",
                    NodeName = "node",
                    FunctionName = "function",
                    Parameters = listOf(
                        CodeProperty(TypeValue = "param1", TypeType = "type1"),
                        CodeProperty(TypeValue = "param2", TypeType = "type2")
                    )
                )
            )
        )

        // When
        val result = analyser.display(function)

        // Then
        val expected: String = """
            @annotation(key1 = value1, key2 = value2)
            function_name(param1: type1, param2: type2) -> return_TYPE {
                // ->package.node.function(param1: type1, param2: type2)
            }
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun shouldReturnContentOfCodeSnippet() {
        // Given
        val lines: List<String> = listOf(
            "fun add(a: Int, b: Int): Int {",
            "    return a + b",
            "}"
        )
        val position: CodePosition = CodePosition(0, 0, 3, 1)

        // When
        val content: String = analyser.contentByPosition(lines, position)

        // Then, the expected content uses newlines and spaces.
        val expectedContent: String = """
            fun add(a: Int, b: Int): Int {
                return a + b
            }
        """.trimIndent()
        assertEquals(expectedContent, content)
    }
}

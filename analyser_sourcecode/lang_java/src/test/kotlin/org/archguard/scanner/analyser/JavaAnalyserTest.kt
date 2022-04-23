package org.archguard.scanner.analyser

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File

internal class JavaAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveDataStructure(any()) } just runs
    }
    private val mockContext = mockk<SourceCodeContext> {
        every { client } returns mockClient
    }

    // print these files and copy to analyser [feat_apicalls/backend] to finish the contract tests
    @Nested
    inner class PrintFilesForApiAnalyserContract {
        @Test
        fun shouldCreateJavaHelloWorldApi() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("spring/HelloController.java").path
            val nodes = JavaAnalyser(mockContext).analyse()
            File("structs_HelloController.json").writeText(Json.encodeToString(nodes))
        }

        @Test
        fun shouldHandleSubController() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("spring/SubController.java").path
            val nodes = JavaAnalyser(mockContext).analyse()
            File("structs_SubController.json").writeText(Json.encodeToString(nodes))
        }

        @Test
        fun shouldHandleErrorSlash() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("spring/NormalController.java").path
            val nodes = JavaAnalyser(mockContext).analyse()
            File("structs_NormalController.json").writeText(Json.encodeToString(nodes))
        }

        @Test
        fun url_method_in_annotation() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("spring/DemoController.java").path
            val nodes = JavaAnalyser(mockContext).analyse()
            File("structs_DemoController.json").writeText(Json.encodeToString(nodes))
        }
    }
}

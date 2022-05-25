package org.archguard.scanner.analyser

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File

internal class TypeScriptAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveDataStructure(any()) } just runs
    }
    private val mockContext = mockk<SourceCodeContext> {
        every { client } returns mockClient
    }

    @AfterEach
    internal fun tearDown() {
        verify { mockClient.saveDataStructure(any()) }
    }

    @Test
    fun `should return nodes of the sample code file`() {
        every { mockContext.path } returns this.javaClass.classLoader.getResource("ts").path

        val result = TypeScriptAnalyser(mockContext).analyse()

        assertThat(result).hasSize(16)
    }

    // print these files and copy to analyser [feat_apicalls/frontend] to finish the contract tests
    @Nested
    inner class PrintFilesForApiAnalyserContract {
        @Test
        fun shouldSupportIdentifyComponentApi() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("ts/apicall").path
            val nodes = TypeScriptAnalyser(mockContext).analyse()
            File("structs_apicall.json").writeText(Json.encodeToString(nodes))
        }

        @Test
        internal fun shouldCorrectComponentName() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("ts/interface-error").path
            val nodes = TypeScriptAnalyser(mockContext).analyse()
            File("structs_interface-error.json").writeText(Json.encodeToString(nodes))
        }

        @Test
        internal fun shouldSaveApiAdapter() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("ts/api-adapter").path
            val nodes = TypeScriptAnalyser(mockContext).analyse()
            File("structs_api-adapter.json").writeText(Json.encodeToString(nodes))
        }

        @Test
        internal fun testForUmi() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("ts/js-umi-request").path
            val nodes = TypeScriptAnalyser(mockContext).analyse()
            File("structs_js-umi-request.json").writeText(Json.encodeToString(nodes))
        }
    }
}

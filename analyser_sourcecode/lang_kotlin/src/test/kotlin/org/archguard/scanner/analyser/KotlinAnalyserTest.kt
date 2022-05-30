package org.archguard.scanner.analyser

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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File

internal class KotlinAnalyserTest {
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
        every { mockContext.path } returns this.javaClass.classLoader.getResource("kotlin").path

        val result = KotlinAnalyser(mockContext).analyse()

        assertThat(result).hasSize(2)
        assertThat(result[0].FilePath.replace("\\", "/")).endsWith("resources/test/kotlin/Hello.kt")
    }

    // print these files and copy to analyser [feat_apicalls/backend] to finish the contract tests
    @Nested
    inner class PrintFilesForApiAnalyserContract {
        @Test
        fun identRestTemplateCall() {
            every { mockContext.path } returns this.javaClass.classLoader.getResource("spring").path
            val nodes = KotlinAnalyser(mockContext).analyse()
            File("structs_kotlin.json").writeText(Json.encodeToString(nodes))
        }
    }
}

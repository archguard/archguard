package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.io.File

internal class ApiCallAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveApi(any()) } just runs
    }
    private val mockContext = mockk<SourceCodeContext> {
        every { client } returns mockClient
    }

    @AfterEach
    internal fun tearDown() {
        verify { mockClient.saveApi(any()) }
    }

    private fun loadNodes(source: String): List<CodeDataStruct> {
        val file = File(this.javaClass.classLoader.getResource(source)!!.file)
        return Json { ignoreUnknownKeys = true }.decodeFromString(file.readText())
    }

    @Test
    fun `should analyse frontend api call in absolute path`() {
        val nodes = loadNodes("2_codes.json")

        every { mockContext.language } returns "typescript"
        every { mockContext.path } returns "/var/folders/w8/3_cj5bnx7hqcw5ghz51dn_9h0000gn/T/archguard11975424402559343478"

        val analyser = ApiCallAnalyser(mockContext)
        val apis = analyser.analyse(nodes)

        File("api.json").writeText(Json.encodeToString(apis))

        assertThat(apis.size).isEqualTo(19)
    }
}

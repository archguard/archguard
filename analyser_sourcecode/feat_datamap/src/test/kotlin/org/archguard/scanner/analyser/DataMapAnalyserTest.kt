package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class DataMapAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveRelation(any()) } just runs
    }
    private val mockContext = mockk<SourceCodeContext> {
        every { client } returns mockClient
        every { withFunctionCode } returns false
        every { language } returns "java"
        every { path } returns "src/test/resources/datastructure"
        every { debug } returns false
    }

    @AfterEach
    internal fun tearDown() {
        verify { mockClient.saveRelation(any()) }
    }

    @Test
    fun should_handle_for_jpa_native_method() {
        val dataString = javaClass.getResource("/datastructure/0_codes.json").readText()
        val data = Json.decodeFromString<List<CodeDataStruct>>(dataString)

        val analyser = DataMapAnalyser(mockContext)
        val relations = analyser.analyse(data)

        relations.size shouldBe 12
    }
}

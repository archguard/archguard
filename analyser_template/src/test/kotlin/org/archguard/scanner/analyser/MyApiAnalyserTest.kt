package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.junit.jupiter.api.Test

internal class MyApiAnalyserTest {
    private val mockClient: ArchGuardClient = mockk {
        every { saveApi(any()) } just runs
    }
    private val mockContext: SourceCodeContext = mockk {
        every { client } returns mockClient
    }

    @Test
    fun `should trigger the save api when analysing the api`() {
        val inputs = listOf(
            CodeDataStruct(NodeName = "MyLanguageAnalyser"),
            CodeDataStruct(NodeName = "MyApiAnalyser"),
        )

        val analyser = MyApiAnalyser(mockContext)
        analyser.analyse(inputs)

        verify {
            mockClient.saveApi(any())
        }
    }
}

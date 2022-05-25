package org.archguard.scanner.analyser

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

internal class DataMapAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveDataStructure(any()) } just runs
    }
    private val mockContext = mockk<SourceCodeContext> {
        every { client } returns mockClient
    }

    @AfterEach
    internal fun tearDown() {
        verify { mockClient.saveRelation(any()) }
    }
}

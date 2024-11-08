package org.archguard.scanner.analyser

import io.mockk.*
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class GoAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveDataStructure(any()) } just runs
    }
    private val mockContext = mockk<SourceCodeContext> {
        every { client } returns mockClient
        every { withFunctionCode } returns false
    }

    @Test
    fun `should return nodes of the sample code file`() {
        every { mockContext.path } returns this.javaClass.classLoader.getResource("go").path

        val result = GoAnalyser(mockContext).analyse()

        Assertions.assertEquals(result.size, 1)
        assert(result[0].FilePath.contains("hello.go"))
    }

    @Test
    fun `should save data structure to client`() {
        every { mockContext.path } returns this.javaClass.classLoader.getResource("gen/db-query.go").path

        val structs = GoAnalyser(mockContext).analyse()

        Assertions.assertEquals(structs.size, 3)
    }
}


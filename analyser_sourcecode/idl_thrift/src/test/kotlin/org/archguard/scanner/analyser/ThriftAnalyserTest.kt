package org.archguard.scanner.analyser

import io.mockk.*
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ThriftAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveDataStructure(any()) } just runs
    }
    private val mockContext = mockk<SourceCodeContext> {
        every { client } returns mockClient
        every { withFunctionCode } returns false
    }

    @AfterEach
    internal fun tearDown() {
        verify { mockClient.saveDataStructure(any()) }
    }

    @Test
    fun `should return nodes of the sample code file`() {
        every { mockContext.path } returns this.javaClass.classLoader.getResource("thrift").path

        val result = ThriftAnalyser(mockContext).analyse()

        Assertions.assertEquals(result.size, 1)
        assert(result[0].FilePath.contains("hello.thrift"))
    }
}


package org.archguard.scanner.analyser

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class PythonAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveDataStructure(any()) } just runs
    }
    private val mockContext = mockk<SourceCodeContext> {
        every { client } returns mockClient
    }

    @Test
    fun `should return nodes of the sample code file`() {
        every { mockContext.path } returns this.javaClass.classLoader.getResource("py").path

        val result = PythonAnalyser(mockContext).analyse()

        Assertions.assertEquals(result.size, 2)
        Assertions.assertEquals(result[0].NodeName, "foo")
    }
}

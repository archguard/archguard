package org.archguard.scanner.analyser

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KotlinAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveDataStructure(any(), any(), any()) } just runs
    }
    private val mockContext = mockk<SourceCodeContext> {
        every { systemId } returns "systemId"
        every { language } returns "kotlin"
        every { client } returns mockClient
    }

    @Test
    fun `should return nodes of the sample code file`() {
        every { mockContext.path } returns this.javaClass.classLoader.getResource("kotlin").path

        val result = KotlinAnalyser(mockContext).analyse()

        assertThat(result).hasSize(2)
        assertThat(result[0].FilePath.replace("\\", "/")).endsWith("resources/test/kotlin/Hello.kt")
    }
}

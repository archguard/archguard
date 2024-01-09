package org.archguard.architecture;

import org.archguard.scanner.core.architecture.ArchitectureContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.io.path.toPath

class ArchitectureAnalyserTest {

    @Test
    fun shouldReturnListOfArchitectureView() {
        // Given
        val resource = this.javaClass.classLoader.getResource("spring-blog")!!.toURI().toPath()
        val analyser = ArchitectureAnalyser(
            SampleArchitectureContext(path = resource.toString())
        )

        // When
        val result = analyser.analyse()

        // Then
        assertEquals(1, result.size)
        assertEquals(ArchitectureView::class, result[0]::class)
    }
}

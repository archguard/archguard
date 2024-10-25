package org.archguard.architecture;

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
        val result = analyser.analyse(language = "java")

        // Then
        println(Json.encodeToString(result))
        assertEquals(1, result.size)
        assertEquals(result[0].conceptArchitecture.domainModels.size, 3)
    }
}

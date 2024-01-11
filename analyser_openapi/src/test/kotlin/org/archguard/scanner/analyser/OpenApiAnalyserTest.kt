package org.archguard.scanner.analyser;

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.EmptyArchGuardClient
import org.archguard.scanner.core.openapi.OpenApiContext
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

data class TestOpenApiContext(
    override val client: ArchGuardClient,
    override val path: String,
    override val branch: String
) : OpenApiContext


class OpenApiAnalyserTest {
    @Test
    fun `should analyse single OpenAPI file`() {

        // Given
        val context = TestOpenApiContext(
            client = EmptyArchGuardClient(),
            path = "",
            branch = ""
        )
        val analyser = OpenApiAnalyser(context)

        // When
        val result = analyser.analyse()

        // Then
        assertTrue(result.size >= 48)
    }
}

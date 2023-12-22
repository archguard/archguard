package org.archguard.scanner.analyser

import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.sca.ScaContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class ScaAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveDependencies(any()) } just runs
    }
    private val mockContext = mockk<ScaContext> {
        every { client } returns mockClient
    }

    @AfterEach
    internal fun tearDown() {
        verify { mockClient.saveDependencies(any()) }
    }

    @Test
    internal fun gradle_with_maven() {
        every { mockContext.path } returns "."
        every { mockContext.language } returns "kotlin"
        val analyser = ScaAnalyser(mockContext)
        val deps = analyser.analyse()

        assert(deps.size >= 2)
    }

    @Test
    internal fun support_archguard_self_toml() {
        every { mockContext.path } returns ".."
        every { mockContext.language } returns "kotlin"
        val analyser = ScaAnalyser(mockContext)
        val deps = analyser.analyse().filter {
            it.depName.contains("chapi")
        }

        println(Json.encodeToString(deps))
        deps[0].depVersion shouldNotBe ""
    }
}

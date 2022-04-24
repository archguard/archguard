package org.archguard.scanner.ctl.command

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ScannerCommandTest {
    private val fixture = ScannerCommand(
        type = AnalyserType.SOURCE_CODE,
        systemId = "0",
        serverUrl = "http://localhost:8080/",
        path = ".",
    )

    @Nested
    inner class ForSourceCodeContext {
        @Test
        fun `should parse the language spec when given the identifier`() {
            val command = fixture.copy(
                language = "kotlin",
                features = listOf("api_calls"),
            )

            assertThat(command.getAnalyserSpecs()).containsOnly(
                OfficialAnalyserSpecs.KOTLIN.spec(),
                OfficialAnalyserSpecs.API_CALLS.spec(),
            )
            assertThat(command.buildSourceCodeContext().language).isEqualTo("kotlin")
        }

        @Test
        fun `should parse the language spec when given the analyser spec`() {
            val command = fixture.copy(
                language = "kotlin",
                features = listOf(OfficialAnalyserSpecs.API_CALLS.spec()),
            )

            assertThat(command.getAnalyserSpecs()).containsOnly(
                OfficialAnalyserSpecs.KOTLIN.spec(),
                OfficialAnalyserSpecs.API_CALLS.spec(),
            )
            assertThat(command.buildSourceCodeContext().language).isEqualTo("kotlin")
        }

        @Test
        fun `should parse the language spec when given the customized analyser spec`() {
            val customized = AnalyserSpec(
                identifier = "identifier",
                host = "host",
                version = "version",
                jar = "jar",
                className = "className",
            )
            val command = fixture.copy(
                language = "kotlin",
                features = listOf("API_CALLS", customized),
            )

            assertThat(command.getAnalyserSpecs()).containsOnly(
                OfficialAnalyserSpecs.KOTLIN.spec(),
                OfficialAnalyserSpecs.API_CALLS.spec(),
                customized,
            )
            assertThat(command.buildSourceCodeContext().language).isEqualTo("kotlin")
        }

        @Test
        fun `should parse the language spec when given the json map`() {
            val customized = AnalyserSpec(
                identifier = "identifier",
                host = "host",
                version = "version",
                jar = "jar",
                className = "className",
            )
            val command = fixture.copy(
                language = "KOTLIN",
                features = listOf("API_CALLS", Json.encodeToString(customized)),
            )

            assertThat(command.getAnalyserSpecs()).containsOnly(
                OfficialAnalyserSpecs.KOTLIN.spec(),
                OfficialAnalyserSpecs.API_CALLS.spec(),
                customized,
            )
            assertThat(command.buildSourceCodeContext().language).isEqualTo("kotlin")
        }
    }
}

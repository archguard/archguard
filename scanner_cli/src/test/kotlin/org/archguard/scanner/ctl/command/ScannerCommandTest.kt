package org.archguard.scanner.ctl.command

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockkConstructor
import io.mockk.runs
import io.mockk.verify
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.ctl.client.ArchGuardConsoleClient
import org.archguard.scanner.ctl.client.ArchGuardCsvClient
import org.archguard.scanner.ctl.client.ArchGuardHttpClient
import org.archguard.scanner.ctl.client.ArchGuardJsonClient
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ScannerCommandTest {
    private val fixture = ScannerCommand(
        type = AnalyserType.SOURCE_CODE,
        systemId = "0",
        serverUrl = "http://localhost:8080/",
        path = ".",
    )

    @AfterEach
    internal fun tearDown() {
        clearAllMocks()
    }

    @Nested
    inner class BuildClientTest {
        @Test
        fun `should build the console client as default`() {
            mockkConstructor(ArchGuardConsoleClient::class)
            every { anyConstructed<ArchGuardConsoleClient>().saveApi(any()) } just runs

            val client = fixture.copy().buildClient()
            client.saveApi(emptyList())

            verify { anyConstructed<ArchGuardConsoleClient>().saveApi(any()) }
        }

        @Test
        fun `should build the client via output parameter`() {
            mockkConstructor(ArchGuardConsoleClient::class)
            mockkConstructor(ArchGuardHttpClient::class)
            mockkConstructor(ArchGuardJsonClient::class)
            mockkConstructor(ArchGuardCsvClient::class)
            every { anyConstructed<ArchGuardConsoleClient>().saveApi(any()) } just runs
            every { anyConstructed<ArchGuardHttpClient>().saveApi(any()) } just runs
            every { anyConstructed<ArchGuardJsonClient>().saveApi(any()) } just runs
            every { anyConstructed<ArchGuardCsvClient>().saveApi(any()) } just runs

            val client = fixture.copy(output = listOf("http", "json", "csv", "console")).buildClient()
            client.saveApi(emptyList())

            verify { anyConstructed<ArchGuardConsoleClient>().saveApi(any()) }
            verify { anyConstructed<ArchGuardHttpClient>().saveApi(any()) }
            verify { anyConstructed<ArchGuardJsonClient>().saveApi(any()) }
            verify { anyConstructed<ArchGuardCsvClient>().saveApi(any()) }
        }
    }
}

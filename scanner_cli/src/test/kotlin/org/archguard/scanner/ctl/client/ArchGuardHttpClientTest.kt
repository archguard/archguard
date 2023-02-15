package org.archguard.scanner.ctl.client

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.archguard.scanner.ctl.command.ScannerCommand
import org.junit.jupiter.api.Test
import java.net.URI

class ArchGuardHttpClientTest {
    @Test
    fun `uri encode for windows`() {
        val command = mockk<ScannerCommand>()
        val path = "C:\\Users\\archguard\\AppData\\Local\\Temp\\archguard15652771582882474328"

        val client = ArchGuardHttpClient("java", "http://localhost:8080", "systemId", path, command)
        val urlString = client.buildUrl("sca-dependencies")

        val url = URI(urlString)
        url.path shouldBe "/api/scanner/systemId/reporting/sca-dependencies"
        url.query shouldBe "language=java&path=$path"
    }
}

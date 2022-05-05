package com.thoughtworks.archguard.smartscanner.infra

import com.thoughtworks.archguard.scanner.infrastructure.command.Processor
import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import com.thoughtworks.archguard.smartscanner.AnalyserType
import com.thoughtworks.archguard.smartscanner.ScannerCommand
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.slot
import io.mockk.unmockkObject
import org.junit.jupiter.api.Test
import java.io.File


class EmptyStreamConsumer : StreamConsumer {
    override fun consumeLine(line: String) {}
}

internal class ScannerCliClientTest {
    private val scannerCliClient = ScannerCliClient()

    private val logStream = EmptyStreamConsumer()

    private val command = ScannerCommand(
        AnalyserType.SOURCE_CODE,
        "2222",
        "http://localhost:8080",
        ".",
        File("test"),
        logStream,
    ).also {
        it.language = "kotlin"
        it.features = listOf("apicalls", "datamap")
    }

    @Test
    fun `should execute the scan command with java jar command`() {
        mockkObject(RemoteFileLoader)
        mockkObject(Processor)

        val slot = slot<ProcessBuilder>()
        every { RemoteFileLoader.load(command.workspace, "scanner_cli.jar", any()) } just runs
        every { Processor.executeWithLogs(capture(slot), command.workspace, command.logStream) } returns 1

        scannerCliClient.send(command)

//        assertThat(slot.captured.command()).isEqualTo(
//            listOf(
//                "java",
//                "-jar",
//                "scanner_cli.jar",
//                "--type=source_code",
//                "--system-id=2222",
//                "--server-url=http://localhost:8080",
//                "--path=.",
//                "--language=kotlin",
//                "--features=apicalls",
//                "--features=datamap",
//                "--output=http",
//                "--output=json",
//            )
//        )

        unmockkObject(RemoteFileLoader, Processor)
    }
}

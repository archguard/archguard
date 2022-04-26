package org.archguard.scanner.ctl

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.runs
import io.mockk.verify
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.loader.AnalyserDispatcher
import org.junit.jupiter.api.Test

internal class RunnerTest {
    private val args = arrayOf(
        "--type=source_code",
        "--system-id=2222",
        "--server-url=http://localhost:8080",
        "--path=.",
        "--language=kotlin",
        "--features=apicalls",
        "--features=datamap",
        "--output=json",
        "--output=csv",
        "--output=console",
    )

    private val dispatcher = mockk<AnalyserDispatcher>()

    @Test
    fun `should save the result to client when calling kotlin analyser`() {
        mockkConstructor(AnalyserDispatcher::class) {
            every { anyConstructed<AnalyserDispatcher>().dispatch(any()) } just runs

            Runner().main(args)

            verify {
                anyConstructed<AnalyserDispatcher>().dispatch(
                    ScannerCommand(
                        AnalyserType.SOURCE_CODE,
                        "2222",
                        "http://localhost:8080",
                        ".",
                        "kotlin",
                        listOf("apicalls", "datamap"),
                        listOf("json", "csv", "console"),
                    )
                )
            }
        }
    }
}



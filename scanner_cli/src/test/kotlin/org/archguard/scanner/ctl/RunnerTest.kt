package org.archguard.scanner.ctl

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockkConstructor
import io.mockk.runs
import io.mockk.verify
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.loader.AnalyserDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class RunnerTest {
    @AfterEach
    internal fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should save the result to client when calling kotlin analyser`() {
        val args = arrayOf(
            "--type=source_code",
            "--system-id=2222",
            "--server-url=http://localhost:8080",
            "--path=.",
            "--output=json",
            "--output=csv",
            "--output=console",
            "--language=kotlin",
            "--features=apicalls",
            "--features=datamap",
        )

        mockkConstructor(AnalyserDispatcher::class) {
            every { anyConstructed<AnalyserDispatcher>().dispatch(any()) } just runs

            Runner().main(args)

            verify {
                anyConstructed<AnalyserDispatcher>().dispatch(
                    ScannerCommand(
                        type = AnalyserType.SOURCE_CODE,
                        systemId = "2222",
                        serverUrl = "http://localhost:8080",
                        path = ".",
                        output = listOf("json", "csv", "console"),
                        language = "kotlin",
                        features = listOf("apicalls", "datamap"),
                    )
                )
            }
        }
    }

    @Test
    fun `should save the result to client when calling git analyser`() {
        val args = arrayOf(
            "--type=git",
            "--system-id=2222",
            "--server-url=http://localhost:8080",
            "--path=.",
            "--output=json",
            "--output=csv",
            "--output=console",
            "--repo-id=repo001",
            "--branch=main",
            "--started-at=1000000",
        )

        mockkConstructor(AnalyserDispatcher::class) {
            every { anyConstructed<AnalyserDispatcher>().dispatch(any()) } just runs

            Runner().main(args)

            verify {
                anyConstructed<AnalyserDispatcher>().dispatch(
                    ScannerCommand(
                        type = AnalyserType.GIT,
                        systemId = "2222",
                        serverUrl = "http://localhost:8080",
                        path = ".",
                        output = listOf("json", "csv", "console"),
                        repoId = "repo001",
                        branch = "main",
                        startedAt = 1000000,
                    )
                )
            }
        }
    }

    @Test
    fun `should save the result to client when calling diff changes analyser`() {
        val args = arrayOf(
            "--type=diff_changes",
            "--system-id=2222",
            "--server-url=http://localhost:8080",
            "--path=.",
            "--output=json",
            "--output=csv",
            "--output=console",
            "--branch=main",
            "--since=5952edc",
            "--until=f3fb4e2",
            "--depth=5",
        )

        mockkConstructor(AnalyserDispatcher::class) {
            every { anyConstructed<AnalyserDispatcher>().dispatch(any()) } just runs

            Runner().main(args)

            verify {
                anyConstructed<AnalyserDispatcher>().dispatch(
                    ScannerCommand(
                        type = AnalyserType.DIFF_CHANGES,
                        systemId = "2222",
                        serverUrl = "http://localhost:8080",
                        path = ".",
                        output = listOf("json", "csv", "console"),
                        branch = "main",
                        since = "5952edc",
                        until = "f3fb4e2",
                        depth = 5,
                    )
                )
            }
        }
    }

    @Test
    fun `should save the result to client when calling sca changes analyser`() {
        val args = arrayOf(
            "--type=sca",
            "--system-id=2222",
            "--server-url=http://localhost:8080",
            "--path=.",
            "--output=json",
            "--output=csv",
            "--output=console",
            "--language=kotlin",
        )

        mockkConstructor(AnalyserDispatcher::class) {
            every { anyConstructed<AnalyserDispatcher>().dispatch(any()) } just runs

            Runner().main(args)

            verify {
                anyConstructed<AnalyserDispatcher>().dispatch(
                    ScannerCommand(
                        type = AnalyserType.SCA,
                        systemId = "2222",
                        serverUrl = "http://localhost:8080",
                        path = ".",
                        output = listOf("json", "csv", "console"),
                        language = "kotlin",
                    )
                )
            }
        }
    }
}

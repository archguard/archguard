package org.archguard.scanner.cost.count

import io.kotest.common.runBlocking
import io.kotest.matchers.ints.shouldBeGreaterThan
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class FileWorkerKtTest {
    @Test
    fun process_by_blocking() {
        runBlocking {
            val current = Paths.get("").toAbsolutePath()
            val summary = worker(current.parent.toString())
            println(summary)

            summary.size shouldBeGreaterThan 4
        }
    }
}
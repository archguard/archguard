package org.archguard.scanner.cost.count

import io.kotest.common.runBlocking
import io.kotest.matchers.ints.shouldBeGreaterThan
import org.junit.jupiter.api.Test

internal class FileWorkerKtTest {
    @Test
    fun process_by_blocking() {
        runBlocking {
            val summary = worker("src")

            summary.size shouldBeGreaterThan 4
        }
    }
}
package org.archguard.scanner.cost.count

import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThan
import org.junit.jupiter.api.Test

internal class FileWorkerKtTest {
    @Test
    fun process_current_project() {
        val summary = process("src/main/kotlin")

        summary.files.size shouldBeGreaterThan  4 // just some number
        summary.lines shouldBeGreaterThan  1000 // just some number
    }
}
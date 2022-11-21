package org.archguard.scanner.cost.count

import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThan
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class FileWorkerKtTest {
    @Test
    @Disabled
    fun process_current_project() {
        val summary = processByDir("src")

        summary.size shouldBeGreaterThan 0

        summary[0].files.size shouldBeGreaterThan  4 // just some number
        summary[0].lines shouldBeGreaterThan  1000 // just some number
    }
}
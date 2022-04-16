package org.archguard.scanner.tbs

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class RunnerKtTest {
    @Test
    internal fun name() {
        Runner().main(arrayOf("--path=."))
        assertTrue(File(OUTPUT_FILE).exists())
    }
}
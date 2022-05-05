package org.archguard.scanner.tbs

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertTrue

internal class RunnerTest {

    @Test
    fun run() {
        val file = File(OUTPUT_FILE)
        Runner().main(arrayOf("--path=."))
        assertTrue(file.exists())

        val content = File(OUTPUT_FILE).readText()
        assertTrue(content.contains("RedundantPrintTest"))
        assertTrue(content.contains("UnknownTest"))
        assertTrue(content.contains("DuplicateAssertTest"))
        assertTrue(content.contains("EmptyTest"))
        assertTrue(content.contains("SleepyTest"))
    }
}
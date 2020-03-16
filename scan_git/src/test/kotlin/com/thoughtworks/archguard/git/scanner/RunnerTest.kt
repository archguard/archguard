package com.thoughtworks.archguard.git.scanner

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

internal class RunnerTest {

    @Test
    fun run() {
        val file = File("output.sql")
        file.deleteOnExit();
        Runner().main(arrayOf("--git-path=../code-scanners", "--branch=master"))
        assertTrue(file.exists())
    }
}
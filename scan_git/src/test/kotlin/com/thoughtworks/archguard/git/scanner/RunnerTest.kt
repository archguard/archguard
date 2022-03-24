package com.thoughtworks.archguard.git.scanner

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

internal class RunnerTest {

    @Test
    @Disabled
    fun run() {
        val file = File("output.sql")
        Runner().main(arrayOf("--path=../", "--branch=master"))
        assertTrue(file.exists())
    }

    @Test
    @Disabled
    fun run_loc() {
        val file = File("loc_output.sql")
        Runner().main(arrayOf("--path=../", "--branch=master",  "--loc=true"))
        assertTrue(file.exists())
    }
}
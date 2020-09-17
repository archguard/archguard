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
//        file.deleteOnExit();
//        val calendar = Calendar.getInstance()
//        calendar.set(2019, 1, 1)
//        Runner().main(arrayOf("--git-path=/Users/ygdong/git/spring-framework", "--branch=master","--after=${calendar.timeInMillis}"))
        Runner().main(arrayOf("--git-path=../", "--branch=master"))
        assertTrue(file.exists())
    }
}
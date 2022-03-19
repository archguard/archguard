package com.thoughtworks.archguard.scanner.sourcecode

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class RunnerTest {

    @Test
    @Disabled
    internal fun run_for_function() {
        System.setProperty("dburl", "jdbc:mysql://localhost:3306/archguard?user=root&password=&useSSL=false")
        Runner().main(listOf(
            "--path=../../archguard-backend",
            "--language=java",
            "--system-id=2"))
    }
}
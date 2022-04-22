package org.archguard.analyser.sca

import org.junit.jupiter.api.Test

internal class RunnerTest {

    @Test
    fun run() {
        Runner().main(arrayOf("--path=..", "--system-id=1", "--language=java"))

    }
}
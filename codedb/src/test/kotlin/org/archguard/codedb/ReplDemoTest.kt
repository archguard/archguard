package org.archguard.codedb

import io.kotest.matchers.shouldBe
import org.archguard.codedb.fitness.KotlinReplWrapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ReplDemoTest {
    private lateinit var compiler: KotlinReplWrapper


    @BeforeEach
    internal fun setUp() {
        this.compiler = KotlinReplWrapper()
    }

    @Test
//    @Disabled
    internal fun simple_eval() {
        compiler.eval("val x = 3")
    }

}
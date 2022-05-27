package org.archguard.aaac.repl

import io.kotest.matchers.shouldBe
import org.archguard.aaac.repl.compiler.FullRepl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DslTest {
    private lateinit var compiler: FullRepl


    @BeforeEach
    internal fun setUp() {
        this.compiler = FullRepl()
    }

    @Test
    internal fun simple_eval() {
        compiler.eval("val x = 3")
        val res = compiler.eval("x*2")
        res.resultValue shouldBe 6
    }

    @Test
    internal fun local_file() {
        compiler.eval(
            """%use archguard

            var layer = layered {
                prefixId("org.archguard")
                component("controller") dependentOn component("service")
                组件("service") 依赖于 组件("repository")
            }
            """
        )

        val res = compiler.eval("layer.components().size")
        res.resultValue shouldBe 3

        val name = compiler.eval("layer.components()[0].name")
        name.resultValue shouldBe "controller"
    }

}
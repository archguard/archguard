package org.archguard.aaac.repl

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class DslTest {
    private val compiler: ArchdocCompiler = ArchdocCompiler()

    @Test
    internal fun simple_eval() {
        compiler.eval("val x = 3")
        val res = compiler.eval("x*2")
        res.resultValue shouldBe 6
    }

    @Disabled
    @Test
    internal fun local_file() {
        compiler.eval(
            """
            @file:DependsOn("org.archguard.scanner:doc-executor:2.0.0-alpha.2")
            import org.archguard.dsl.*
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
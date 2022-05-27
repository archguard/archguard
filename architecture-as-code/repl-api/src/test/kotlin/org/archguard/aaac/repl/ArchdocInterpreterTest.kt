package org.archguard.aaac.repl

import org.archguard.aaac.api.InterpreterRequest
import org.archguard.aaac.api.InterpreterService
import org.archguard.aaac.api.messaging.ErrorContent
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ArchdocInterpreterTest {
    private lateinit var interpreter: InterpreterService;

    @BeforeEach
    internal fun setUp() {
        interpreter = ArchdocInterpreter(false)
    }

    @Test
    internal fun mvc_layered() {
        val request = InterpreterRequest(
            code = """%use archguard

layered {
    prefixId("org.archguard")
    component("controller") dependentOn component("service")
    组件("service") 依赖于 组件("repository")
}

        """.trimIndent()
        )
        val result = interpreter.eval(request)

        assertEquals("archguard_graph", result.msgType.type)
    }

    @Test
    internal fun layered_relation() {
        val request = InterpreterRequest(
            code = """%use archguard

layered {
    prefixId("org.archguard")
    component("controller") dependentOn component("service")
    组件("service") 依赖于 组件("repository")
}

context.layered.relations()
        """.trimIndent()
        )
        val result = interpreter.eval(request)

        assertEquals("none", result.msgType.type)
        assertEquals(
            "[{\"source\":\"controller\",\"target\":\"service\"}, {\"source\":\"service\",\"target\":\"repository\"}]",
            result.resultValue
        )
    }

    @Test
    internal fun handle_error() {
        val request = InterpreterRequest(
            code = """%use archguard
layered []

        """.trimIndent()
        )
        val result = interpreter.eval(request)

        assertEquals("error", result.msgType.type)
        val errorMsg =
            "org.jetbrains.kotlinx.jupyter.exceptions.ReplCompilerException: Line_1.jupyter-kts (2:10 - 10) Expecting an index element"
        assertEquals(errorMsg, (result.content as ErrorContent).message)
        assertEquals(
            "org.jetbrains.kotlinx.jupyter.exceptions.ReplCompilerException",
            (result.content as ErrorContent).exception
        )
    }

    @Test
    internal fun handle_scan() {
        val request = InterpreterRequest(
            code = """%use archguard

scan("Backend").create()
        """.trimIndent()
        )
        val result = interpreter.eval(request)

        assertEquals("none", result.msgType.type)
        assertEquals(
            "{\"name\":\"Backend\",\"branch\":\"master\",\"features\":[],\"languages\":[],\"specs\":[]}",
            result.action!!.data
        )
    }
}

package org.archguard.aaac.repl

import org.archguard.aaac.api.InterpreterRequest
import org.archguard.aaac.api.InterpreterService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class ArchdocInterpreterTest {
    private lateinit var interpreter: InterpreterService;

    @BeforeEach
    internal fun setUp() {
        interpreter = ArchdocInterpreter()
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

        assert(result.isArchGuardAaac)
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

        assertFalse(result.isArchGuardAaac)
        assertEquals(
            "[{\"source\":\"controller\",\"target\":\"service\"}, {\"source\":\"service\",\"target\":\"repository\"}]",
            result.resultValue
        )
    }
}

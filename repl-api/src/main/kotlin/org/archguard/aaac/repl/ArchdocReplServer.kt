package org.archguard.aaac.repl

import kotlinx.serialization.Serializable
import org.archguard.dsl.Action

@Serializable
data class ReplResult(
    var resultValue: String,
    var isArchdocApi: Boolean = false,
    var className: String = "",
    var actionData: String = "",
    var action: Action? = null
)

// todo: setup websocket server
class ArchdocReplServer {
    private val compiler: ArchdocCompiler = ArchdocCompiler()

    fun interpret(code: String, context: ReplContext) {
        runWithOutput(code, context.out)
    }

    private fun runWithOutput(code: String, out: ReplOutput) {
        this.eval(code, 0)
    }

    fun eval(code: String, id: Int): ReplResult {
        // todo: return error results
        val result = compiler.eval(code, null, id)
        val resultValue = result.resultValue
        val replResult = ReplResult(
            resultValue.toString()
        )

        // handle action data
        when(resultValue) {
            is Action -> {
                replResult.action = resultValue
                replResult.actionData = resultValue.data
            }
        }

        // todo: return callback action
        val className: String = resultValue?.javaClass?.name.orEmpty()
        if (className.startsWith("org.archguard.dsl")) {
            replResult.isArchdocApi = true
        }

        replResult.className = className

        return replResult
    }
}
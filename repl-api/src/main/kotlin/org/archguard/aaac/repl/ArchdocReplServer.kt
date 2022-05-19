package org.archguard.aaac.repl

import org.archguard.aaac.client.ExecuteResult
import org.archguard.dsl.Action

// todo: setup websocket server
class ArchdocReplServer {
    private val compiler: ArchdocCompiler = ArchdocCompiler()

    fun eval(code: String, id: Int): ExecuteResult {
        // todo: return error results
        val result = compiler.eval(code, null, id)
        val resultValue = result.resultValue
        val executeResult = ExecuteResult(
            resultValue.toString()
        )

        // handle action data
        when(resultValue) {
            is Action -> {
                executeResult.action = resultValue
                executeResult.actionData = resultValue.data
            }
        }

        // todo: return callback action
        val className: String = resultValue?.javaClass?.name.orEmpty()
        if (className.startsWith("org.archguard.dsl")) {
            executeResult.isArchdocApi = true
        }

        executeResult.className = className

        return executeResult
    }
}
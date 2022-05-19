package org.archguard.aaac.repl

import org.archguard.aaac.model.ReplResult
import org.archguard.dsl.Action

// todo: setup websocket server
class ArchdocReplServer {
    private val compiler: ArchdocCompiler = ArchdocCompiler()

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
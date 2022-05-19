package org.archguard.aaac.repl

import org.archguard.aaac.client.EvalRequest
import org.archguard.aaac.client.ExecuteResult
import org.archguard.aaac.client.ReplService
import org.archguard.dsl.Action

// todo: setup websocket server
class ArchdocReplService: ReplService {
    private val compiler: ArchdocCompiler = ArchdocCompiler()

    override fun eval(evalRequest: EvalRequest): ExecuteResult {
        // todo: return error results
        val result = compiler.eval(evalRequest.code, null, evalRequest.id)
        val resultValue = result.resultValue
        val executeResult = ExecuteResult(
            resultValue.toString()
        )

        // handle action data
        when (resultValue) {
            is Action -> {
                executeResult.action = resultValue
                executeResult.actionData = resultValue.data
            }
        }

        val className: String = resultValue?.javaClass?.name.orEmpty()
        if (className.startsWith("org.archguard.dsl")) {
            executeResult.isArchGuardAaac = true
        }

        executeResult.className = className
        return executeResult
    }
}
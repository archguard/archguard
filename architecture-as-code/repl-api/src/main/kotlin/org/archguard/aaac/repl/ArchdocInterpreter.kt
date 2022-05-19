package org.archguard.aaac.repl

import org.archguard.aaac.api.InterpreterRequest
import org.archguard.aaac.api.InterpreterResult
import org.archguard.aaac.api.InterpreterService
import org.archguard.dsl.model.ReactiveAction

class ArchdocInterpreter : InterpreterService {
    private val compiler: ArchdocCompiler = ArchdocCompiler()

    override fun eval(interpreterRequest: InterpreterRequest): InterpreterResult {
        // todo: return error results
        val result = compiler.eval(interpreterRequest.code, null, interpreterRequest.id)
        val resultValue = result.resultValue
        val interpreterResult = InterpreterResult(
            resultValue.toString()
        )

        // handle action data
        when (resultValue) {
            is ReactiveAction -> {
                interpreterResult.action = resultValue
                interpreterResult.actionData = resultValue.data
            }
        }

        val className: String = resultValue?.javaClass?.name.orEmpty()
        if (className.startsWith("org.archguard.dsl")) {
            interpreterResult.isArchGuardAaac = true
        }

        interpreterResult.className = className
        return interpreterResult
    }
}
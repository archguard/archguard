package org.archguard.aaac.repl

import org.archguard.aaac.api.InterpreterRequest
import org.archguard.aaac.api.messaging.Message
import org.archguard.aaac.api.InterpreterService
import org.archguard.aaac.api.messaging.AaacMessageType
import org.archguard.dsl.model.ReactiveAction
import org.jetbrains.kotlinx.jupyter.exceptions.ReplCompilerException
import org.jetbrains.kotlinx.jupyter.repl.EvalResult

class ArchdocInterpreter : InterpreterService {
    private val compiler: ArchdocCompiler = ArchdocCompiler()

    override fun eval(interpreterRequest: InterpreterRequest): Message {
        try {
            val result = compiler.eval(interpreterRequest.code, null, interpreterRequest.id)
            return convertResult(result)
        } catch (e: ReplCompilerException) {
            return Message(e.toString(), "", AaacMessageType.ERROR)
        }
    }

    private fun convertResult(result: EvalResult): Message {
        val resultValue = result.resultValue
        val className: String = resultValue?.javaClass?.name.orEmpty()

        val message = Message(
            resultValue.toString(),
            className
        )

        // handle action data
        when (resultValue) {
            is ReactiveAction -> {
                message.action = resultValue
            }
        }

        if (className.startsWith("org.archguard.dsl")) {
            message.msgType = AaacMessageType.ARCHGUARD_GRAPH
        }

        return message
    }
}
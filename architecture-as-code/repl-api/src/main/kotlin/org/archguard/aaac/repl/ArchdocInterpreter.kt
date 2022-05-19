package org.archguard.aaac.repl

import org.archguard.aaac.api.InterpreterRequest
import org.archguard.aaac.api.messaging.Message
import org.archguard.aaac.api.InterpreterService
import org.archguard.aaac.api.messaging.AaacMessageType
import org.archguard.dsl.model.ReactiveAction

class ArchdocInterpreter : InterpreterService {
    private val compiler: ArchdocCompiler = ArchdocCompiler()

    override fun eval(interpreterRequest: InterpreterRequest): Message {
        // todo: return error results
        val result = compiler.eval(interpreterRequest.code, null, interpreterRequest.id)
        val resultValue = result.resultValue
        val message = Message(
            resultValue.toString()
        )

        // handle action data
        when (resultValue) {
            is ReactiveAction -> {
                message.action = resultValue
            }
        }

        val className: String = resultValue?.javaClass?.name.orEmpty()
        if (className.startsWith("org.archguard.dsl")) {
            message.msgType = AaacMessageType.ARCHGUARD_GRAPH
        }

        message.className = className
        return message
    }
}
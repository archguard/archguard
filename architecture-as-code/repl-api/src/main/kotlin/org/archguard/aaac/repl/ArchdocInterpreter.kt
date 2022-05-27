package org.archguard.aaac.repl

import org.archguard.aaac.api.InterpreterRequest
import org.archguard.aaac.api.InterpreterService
import org.archguard.aaac.api.messaging.AaacMessageType
import org.archguard.aaac.api.messaging.ErrorContent
import org.archguard.aaac.api.messaging.Message
import org.archguard.aaac.repl.compiler.BaseRepl
import org.archguard.aaac.repl.compiler.FullRepl
import org.archguard.aaac.repl.compiler.SlimRepl
import org.archguard.dsl.base.model.ReactiveAction
import org.jetbrains.kotlinx.jupyter.repl.EvalResult

class ArchdocInterpreter(isFullRepl: Boolean = true) : InterpreterService {
    private var compiler: BaseRepl

    init {
        if (isFullRepl) {
            this.compiler = FullRepl()
        } else {
            this.compiler = SlimRepl()
        }
    }

    override fun eval(interpreterRequest: InterpreterRequest): Message {
        try {
            val result = compiler.eval(interpreterRequest.code, null, interpreterRequest.id)
            return convertResult(result, interpreterRequest.id)
        } catch (e: Exception) {
            val content = ErrorContent(e.javaClass.name, e.toString())
            return Message(interpreterRequest.id, "", "", AaacMessageType.ERROR, content = content)
        }
    }

    private fun convertResult(result: EvalResult, id: Int): Message {
        val resultValue = result.resultValue
        val className: String = resultValue?.javaClass?.name.orEmpty()

        val message = Message(
            id,
            resultValue.toString(),
            className
        )

        when (resultValue) {
            is ReactiveAction -> {
                message.action = resultValue
            }
        }

        if (className.startsWith("org.archguard.dsl.design")) {
            message.msgType = AaacMessageType.ARCHGUARD_GRAPH
        }

        return message
    }
}
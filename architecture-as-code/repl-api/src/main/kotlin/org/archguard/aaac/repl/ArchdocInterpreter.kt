package org.archguard.aaac.repl

import org.archguard.aaac.api.InterpreterRequest
import org.archguard.aaac.api.InterpreterService
import org.archguard.aaac.api.messaging.ErrorContent
import org.archguard.aaac.api.messaging.Message
import org.archguard.aaac.api.messaging.MessageType
import org.archguard.aaac.repl.compiler.KotlinReplWrapper
import org.archguard.dsl.base.model.ReactiveAction
import org.jetbrains.kotlinx.jupyter.repl.EvalResultEx
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ArchdocInterpreter : InterpreterService {
    private var compiler: KotlinReplWrapper = KotlinReplWrapper()
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun eval(interpreterRequest: InterpreterRequest): Message {
        try {
            val result = compiler.eval(interpreterRequest.code, interpreterRequest.id)
            return convertResult(result, interpreterRequest.id)
        } catch (e: Exception) {
            logger.error(e.toString())
            val content = ErrorContent(e.javaClass.name, e.toString())
            return Message(interpreterRequest.id, "", "", MessageType.ERROR, content = content)
        }
    }

    private fun convertResult(result: EvalResultEx, id: Int): Message {
        val resultValue = result.rawValue
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
            message.msgType = MessageType.ARCHGUARD_GRAPH
        }

        return message
    }
}
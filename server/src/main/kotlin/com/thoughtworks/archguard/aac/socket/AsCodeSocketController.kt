package com.thoughtworks.archguard.aac.socket

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.aaac.api.InterpreterRequest
import org.archguard.aaac.repl.ArchdocInterpreter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import javax.websocket.OnClose
import javax.websocket.OnError
import javax.websocket.OnMessage
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.server.ServerEndpoint

object ReplService {
    var interpreter: ArchdocInterpreter = ArchdocInterpreter()
}

@ServerEndpoint(value = "/ascode")
@Controller
class AsCodeSocketController {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private var replServer: ArchdocInterpreter = ReplService.interpreter

    @OnOpen
    fun onOpen(session: Session) {
        logger.info("onOpen WebSocket")
    }

    @OnClose
    fun onClose(session: Session) {
        logger.info("onClose WebSocket")
    }

    @OnMessage
    fun onMessage(message: String, session: Session) {
        logger.info("onMessage: $message")
        val request: InterpreterRequest = Json.decodeFromString(message)
        val result = replServer.eval(request)
        session.asyncRemote.sendText(Json.encodeToString(result))
    }

    @OnError
    fun onError(session: Session?, error: Throwable) {
    }
}

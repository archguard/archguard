package org.archguard.archdoc.plugins

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.archdoc.CodeEvalContext
import org.archguard.archdoc.Connection
import org.archguard.archdoc.repl.ArchdocReplServer
import java.time.Duration
import java.util.*

fun Application.configureSockets() {
    val replServer = ArchdocReplServer()
    var id = 0

    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/") {
            val thisConnection = Connection()
            connections += thisConnection
            id += 1
            try {
                val codeEvalContext = receiveDeserialized<CodeEvalContext>()
                val result = replServer.eval(codeEvalContext.code, id)
                send(Json.encodeToString(result))
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }
    }
}
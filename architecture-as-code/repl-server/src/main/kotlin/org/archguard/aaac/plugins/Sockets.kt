package org.archguard.aaac.plugins

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.aaac.api.InterpreterRequest
import org.archguard.aaac.Connection
import org.archguard.aaac.repl.ArchdocInterpreter
import java.time.Duration
import java.util.*

fun Application.configureSockets() {
    val replServer = ArchdocInterpreter()

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

            try {
                val interpreterRequest = receiveDeserialized<InterpreterRequest>()
                val result = replServer.eval(interpreterRequest)

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
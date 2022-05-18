package org.archguard.aaac

import io.ktor.server.application.*
import org.archguard.aaac.plugins.configureRouting
import org.archguard.aaac.plugins.configureSockets

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// sample from: https://github.com/ktorio/ktor-documentation/tree/main/codeSnippets/snippets/tutorial-websockets-server
fun Application.module() {
    configureRouting()
    configureSockets()
}

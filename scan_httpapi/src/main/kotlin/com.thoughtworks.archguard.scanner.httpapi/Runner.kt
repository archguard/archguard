package com.thoughtworks.archguard.scanner.httpapi

import chapi.app.analyser.TypeScriptAnalyserApp
import chapi.app.frontend.ComponentHttpCallInfo
import chapi.app.frontend.FrontendApiAnalyser
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import java.io.File

class Runner : CliktCommand(help = "scan git to sql") {
    private val path: String by option(help = "local path").default(".")

    override fun run() {
        val path = File(path).absolutePath
        val nodes = TypeScriptAnalyserApp().analysisNodeByPath(path)
        val componentCalls: Array<ComponentHttpCallInfo> = FrontendApiAnalyser().analysis(nodes, path)
//        File("api.json").writeText(Json.encodeToString(componentCalls))
    }
}

fun main(args: Array<String>) = Runner().main(args)


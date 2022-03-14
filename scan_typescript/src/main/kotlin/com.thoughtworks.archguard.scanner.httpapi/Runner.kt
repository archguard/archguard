package com.thoughtworks.archguard.scanner.httpapi

import chapi.app.analyser.TypeScriptAnalyserApp
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import java.io.File

class Runner : CliktCommand(help = "scan git to sql") {
    private val path: String by option(help = "local path").default(".")

    override fun run() {
        val path = File(path).absolutePath
        val nodes = TypeScriptAnalyserApp().analysisNodeByPath(path)
    }
}

fun main(args: Array<String>) = Runner().main(args)


package com.thoughtworks.archguard.scanner.javasource

import chapi.app.analyser.JavaAnalyserApp
import chapi.domain.core.CodeDataStruct
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option

class Runner : CliktCommand(help = "scan git to sql") {
    private val path: String by option(help = "local path").default(".")

    override fun run() {
        val dataStructs = JavaAnalyserApp().analysisNodeByPath(path)
        toSql(dataStructs)
    }

    private fun toSql(dataStructs: Array<CodeDataStruct>) {
        println("data structs: " + dataStructs[0].toString())
    }
}

fun main(args: Array<String>) = Runner().main(args)


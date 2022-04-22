package org.archguard.analyser.sca

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option

class Runner : CliktCommand() {
    private val project: String by option(help = "the path of target project").default(".")
    private val systemId: String by option(help = "system id").default("0")
    private val language: String by option(help = "language").default("")

    override fun run() {

    }
}

fun main(args: Array<String>) = Runner().main(args)



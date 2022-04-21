package org.archguard.analyser.sca

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option

class Runner : CliktCommand() {
    private val project: String by option(help = "the path of target project").default(".")

    override fun run() {
        println(project)
    }
}

fun main(args: Array<String>) = Runner().main(args)



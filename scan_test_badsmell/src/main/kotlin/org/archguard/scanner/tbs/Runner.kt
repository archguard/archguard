package org.archguard.scanner.tbs

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import java.io.File

class Runner : CliktCommand(help = "collect test bad smell") {
    private val path: String by option(help = "local path").default(".")

    override fun run() {
        val results = TbsAnalyser().analysisByPath(path)
        File("bs.json").writeText(results.toList().toString())
    }
}

fun main(args: Array<String>) = Runner().main(args)


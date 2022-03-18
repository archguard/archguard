package com.thoughtworks.archguard.scanner.jacoco

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option

class Runner : CliktCommand() {
    private val targetProject: String by option(help = "the path of target proejct").default(".")
    private val bin: String by option(help = "relative path of binary").default("build/classes")
    private val exec: String by option(help = "jacoco data file").default("build/jacoco（/jacoco.exec")

    override fun run() {
        Service(Bean2Sql()).readJacoco(Config(projectPath = targetProject, bin = bin, exec = exec))
    }
}

fun main(args: Array<String>) = Runner().main(args)



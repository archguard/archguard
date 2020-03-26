package com.thoghtworks.archguard.scan_jacoco

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.thoghtworks.archguard.scan_jacoco.helper.Bean2Sql

class Runner : CliktCommand() {
    private val targetProject: String by option(help = "the path of target proejct").default("./scan-jacoco")
    private val bin: String by option(help = "relative path of binary").default("target/classes")
    private val exec: String by option(help = "jacoco data file").default("target/jacoco.exec")

    override fun run() {
        Service(Bean2Sql()).readJacoco(Config(projectPath = targetProject, bin = bin, exec = exec))
    }
}

fun main(args: Array<String>) = Runner().main(args)



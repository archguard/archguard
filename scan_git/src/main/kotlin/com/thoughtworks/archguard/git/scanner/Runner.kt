package com.thoughtworks.archguard.git.scanner

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql

class Runner : CliktCommand(help = "scan git to sql") {
    private val branch: String by option(help = "git repository branch").default("master")
    private val after: String by option(help = "scanner only scan commits after this timestamp").default("0")

    private val path: String by option(help = "local path").default(".")
    private val repoId: String by option(help = "repo id").default("0")
    private val systemId: String by option(help = "system id").default("0")
    private val language: String by option(help = "language").default("")
    // todo: use language services to count line
    private val loc: String? by option(help = "scan loc")

    override fun run() {
        val service = ScannerService(JGitAdapter(language), Bean2Sql())
        service.git2SqlFile(path, branch, after, repoId, systemId.toLong())
    }
}

fun main(args: Array<String>) = Runner().main(args)



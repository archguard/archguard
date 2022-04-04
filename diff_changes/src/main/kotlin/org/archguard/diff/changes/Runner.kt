package org.archguard.diff.changes

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option

class Runner : CliktCommand() {
    private val branch: String by option(help = "git repository branch").default("master")
    private val since: String by option(help="since specific revision").default("0")
    private val util: String by option(help="util specific revision").default("0")

    private val path: String by option(help = "local path").default(".")

    private val systemId: String by option(help = "system id").default("0")
    private val language: String by option(help = "language").default("")

    override fun run() {
        val differ = GitDiffer(path, branch, systemId, language)
        differ.countInRange(since, util)
    }
}

fun main(args: Array<String>) = Runner().main(args)


package com.thoughtworks.archguard.git.scanner

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.thoughtworks.archguard.git.scanner.complexity.CognitiveComplexityParser
import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql

class Runner : CliktCommand() {
    private val gitPath: String by option(help = "git repository local path").default("scan_git/test_data")
    private val branch: String by option(help = "git repository branch").default("master")
    private val after: String by option(help = "scanner only scan commits after this timestamp").default("0")

    override fun run() {
        val service = ScannerService(JGitAdapter(CognitiveComplexityParser()), Bean2Sql())
        service.git2SqlFile(Config(gitPath, branch, after))
    }
}

fun main(args: Array<String>) = Runner().main(args)



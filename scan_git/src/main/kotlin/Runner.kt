package com.thoughtworks.archguard.git.scanner

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.thoughtworks.archguard.git.scanner.complexity.CognitiveComplexityParser

class Runner : CliktCommand() {
    private val gitPath: String by option(help = "git path").default("scan_git/test_data")
    private val branch: String by option(help = "git branch").default("master")

    override fun run() {
        val service = ScannerService(JGitAdapter(CognitiveComplexityParser()))
        service.git2SqlFile(Config(gitPath, branch))
    }
}

fun main(args: Array<String>) = Runner().main(args)



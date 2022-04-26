package org.archguard.doc.generator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option

class Runner : CliktCommand() {
    override fun run() {
        RuleDocGenerator().execute()
    }
}

fun main(args: Array<String>) = Runner().main(args)


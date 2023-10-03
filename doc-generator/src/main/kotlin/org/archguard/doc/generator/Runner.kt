package org.archguard.doc.generator

import com.github.ajalt.clikt.core.CliktCommand

class Runner : CliktCommand() {
    override fun run() {
        RuleDocGenerator().execute()
    }
}

fun main(args: Array<String>) = Runner().main(args)


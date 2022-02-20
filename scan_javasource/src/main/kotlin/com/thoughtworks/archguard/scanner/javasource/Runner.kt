package com.thoughtworks.archguard.scanner.javasource

import com.github.ajalt.clikt.core.CliktCommand

class Runner : CliktCommand(help = "scan git to sql") {
    override fun run() {
        println("hello, world");
    }
}

fun main(args: Array<String>) = Runner().main(args)


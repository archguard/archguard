package com.thoughtworks.archguard.git.analyzer

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Runner : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
    }


}

fun main(args: Array<String>) {
    runApplication<Runner>(*args)
}
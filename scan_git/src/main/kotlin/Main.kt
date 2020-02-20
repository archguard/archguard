package com.thoughtworks.archguard.git.scanner

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
open class Main(@Autowired val service: ScannerService) : ApplicationRunner {
    val log: Logger = LoggerFactory.getLogger(Main::class.java)

    override fun run(args: ApplicationArguments?) {
        val gitPath = args?.getOptionValues("gitPath")?.get(0)
        val branch = args?.getOptionValues("branch")?.get(0)
        service.git2SqlFile(Config(gitPath ?: "test_data", branch ?: "master"))
    }
}

fun main(args: Array<String>) {
    val context = runApplication<Main>(*args)
}



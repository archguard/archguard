package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.complexity.CognitiveComplexityParser
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
open class Runner : ApplicationRunner {
    var gitPath: String = "scan_git/test_data"
    var branch: String = "master"


    override fun run(args: ApplicationArguments?) {
        gitPath = args?.getOptionValues("gitPath")?.get(0) ?: gitPath
        branch = args?.getOptionValues("branch")?.get(0) ?: branch
    }


    @Bean
    open fun parserBean(): CognitiveComplexityParser {
        return CognitiveComplexityParser()
    }

}

fun main(args: Array<String>) {
    val context = runApplication<Runner>(*args)
    val service = context.getBean(ScannerService::class.java)
    val runner = context.getBean(Runner::class.java)
    service.git2SqlFile(Config(runner.gitPath, runner.branch))
}



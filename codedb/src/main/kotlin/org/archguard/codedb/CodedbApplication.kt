package org.archguard.codedb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CodedbApplication

fun main(args: Array<String>) {
	runApplication<CodedbApplication>(*args)
}

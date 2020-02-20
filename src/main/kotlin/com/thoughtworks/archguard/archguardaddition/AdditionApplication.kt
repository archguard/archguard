package com.thoughtworks.archguard.archguardaddition

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AdditionApplication

fun main(args: Array<String>) {
	runApplication<AdditionApplication>(*args)
}

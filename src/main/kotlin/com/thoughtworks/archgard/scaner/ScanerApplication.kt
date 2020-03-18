package com.thoughtworks.archgard.scaner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScanerApplication

fun main(args: Array<String>) {
	runApplication<ScanerApplication>(*args)
}

package com.thoughtworks.archgard.scanner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScannerApplication

fun main(args: Array<String>) {
    runApplication<ScannerApplication>(*args)
}

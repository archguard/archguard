package com.thoughtworks.archgard.scanner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class ScannerApplication

fun main(args: Array<String>) {
    runApplication<ScannerApplication>(*args)
}

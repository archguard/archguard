package com.thoughtworks.archguard.git.scanner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
open class Main

fun main(args: Array<String>) {
    val context = runApplication<Main>(*args)
    val service = context.getBean(ScannerService::class.java)
    val config = Config(path = "/Users/ygdong/Downloads/gittest")
    service.scan(config)

}



package org.archguard.codedb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [MongoReactiveDataAutoConfiguration::class])
class CodedbApplication

fun main(args: Array<String>) {
    runApplication<CodedbApplication>(*args)
}

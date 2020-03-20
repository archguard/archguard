package com.thoughtworks.archguard

import org.jdbi.v3.core.Jdbi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
class Application {
    @Bean
    fun getDB(dataSource: DataSource): Jdbi {
		return Jdbi.create(dataSource)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

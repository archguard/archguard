package com.thoughtworks.archguard.git.analyzer

import org.jdbi.v3.spring4.JdbiFactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
open class Runner : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
    }


    @Bean
    open fun jdbiFactory(@Autowired ds: DataSource): JdbiFactoryBean {
        return JdbiFactoryBean(ds)
    }

}

fun main(args: Array<String>) {
    runApplication<Runner>(*args)
}
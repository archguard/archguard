package com.thoughtworks.archguard.dependence_package.infrastracture

import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource


@Configuration
class DB {

    @Value("\${spring.datasource.url}")
    private lateinit var url: String

    @Value("\${spring.datasource.username}")
    private lateinit var user: String

    @Value("\${spring.datasource.password}")
    private lateinit var password: String

    @Bean
    fun getDBI(): Jdbi {
        val dataSource = DriverManagerDataSource()
        dataSource.url = url
        dataSource.username = user
        dataSource.password = password
        return Jdbi.create(dataSource)
    }
}

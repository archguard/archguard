package com.thoughtworks.archguard

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.spi.JdbiPlugin
import org.jdbi.v3.spring4.JdbiFactoryBean
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.oas.annotations.EnableOpenApi
import javax.sql.DataSource

@SpringBootApplication
@EnableOpenApi
class Application {
    @Bean
    fun jdbiFactory(@Autowired ds: DataSource): JdbiFactoryBean {
        val factoryBean = JdbiFactoryBean(ds)
        factoryBean.setAutoInstallPlugins(true)
        return factoryBean
    }

    @Bean
    fun jdbi(@Autowired factoryBean: JdbiFactoryBean): Jdbi {
        factoryBean.setAutoInstallPlugins(true)
        return factoryBean.`object`
    }

    @Bean
    fun jdbiPlugins(): List<JdbiPlugin> {
        return listOf(SqlObjectPlugin(), KotlinPlugin(), KotlinSqlObjectPlugin())
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

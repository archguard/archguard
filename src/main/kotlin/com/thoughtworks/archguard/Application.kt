package com.thoughtworks.archguard

import com.thoughtworks.archguard.project_info.infrastracture.AESCrypt
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.spring4.JdbiFactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
class Application {
    @Bean
    open fun jdbiFactory(@Autowired ds: DataSource): JdbiFactoryBean {
        val factoryBean = JdbiFactoryBean(ds)
        factoryBean.setAutoInstallPlugins(true)
        return factoryBean
    }

    @Bean
    open fun jdbi(@Autowired factoryBean: JdbiFactoryBean): Jdbi {
        factoryBean.setAutoInstallPlugins(true)
        return factoryBean.`object`
    }

//    @Bean
//    fun aesCrypt(): AESCrypt {
//        return AESCrypt("thoughtworks.com")
//    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

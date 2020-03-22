package com.thoughtworks.archgard

import com.thoughtworks.archgard.hub.domain.service.HubService
import com.thoughtworks.archgard.scanner.infrastructure.db.BadSmellDao
import com.thoughtworks.archgard.scanner.infrastructure.db.TestBadSmellDao
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.spi.JdbiPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import java.util.function.Consumer
import javax.sql.DataSource


@SpringBootApplication
class ScannerApplication {

    @Bean
    fun jdbi(ds: DataSource, jdbiPlugins: List<JdbiPlugin>): Jdbi {
        val proxy = TransactionAwareDataSourceProxy(ds!!)
        val jdbi = Jdbi.create(proxy)
        jdbiPlugins.forEach(Consumer { plugin: JdbiPlugin? -> jdbi.installPlugin(plugin) })
        return jdbi
    }

    @Bean
    fun jdbiPlugins(): List<JdbiPlugin> {
        return listOf(SqlObjectPlugin(), KotlinPlugin(), KotlinSqlObjectPlugin())
    }

    @Bean
    fun badSmellModelDao(jdbi: Jdbi): BadSmellDao {
        return jdbi.onDemand(BadSmellDao::class.java)
    }

    @Bean
    fun testBadSmellModelDao(jdbi: Jdbi): TestBadSmellDao {
        return jdbi.onDemand(TestBadSmellDao::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<ScannerApplication>(*args)
}

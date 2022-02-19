package com.thoughtworks.archguard

import com.thoughtworks.archgard.scanner.infrastructure.db.*
import com.thoughtworks.archgard.scanner2.infrastructure.mysql.ClassMetricsDao
import com.thoughtworks.archgard.scanner2.infrastructure.mysql.MethodMetricsDao
import com.thoughtworks.archgard.scanner2.infrastructure.mysql.ModuleMetricsDao
import com.thoughtworks.archgard.scanner2.infrastructure.mysql.PackageMetricsDao
import com.thoughtworks.archguard.report.infrastructure.GitHotFileDao
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.spi.JdbiPlugin
import org.jdbi.v3.spring4.JdbiFactoryBean
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import springfox.documentation.oas.annotations.EnableOpenApi
import java.util.*
import java.util.function.Consumer
import javax.sql.DataSource

@SpringBootApplication
@EnableOpenApi
//@EnableEurekaClient
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
    fun gitHotFileDao(jdbi: Jdbi): GitHotFileDao {
        return jdbi.onDemand(GitHotFileDao::class.java)
    }

    @Bean
    fun jdbi(ds: DataSource, jdbiPlugins: List<JdbiPlugin>): Jdbi {
        val proxy = TransactionAwareDataSourceProxy(ds)
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

    @Bean
    fun checkStylesDao(jdbi: Jdbi): StyleDao {
        return jdbi.onDemand(StyleDao::class.java)
    }

    @Bean
    fun overviewDao(jdbi: Jdbi): OverviewDao {
        return jdbi.onDemand(OverviewDao::class.java)
    }

    @Bean
    fun configDao(jdbi: Jdbi): ConfigDao {
        return jdbi.onDemand(ConfigDao::class.java)
    }

    @Bean
    fun classMetricsDao(jdbi: Jdbi): ClassMetricsDao {
        return jdbi.onDemand(ClassMetricsDao::class.java)
    }

    @Bean
    fun methodMetricsDao(jdbi: Jdbi): MethodMetricsDao {
        return jdbi.onDemand(MethodMetricsDao::class.java)
    }

    @Bean
    fun packageMetricsDao(jdbi: Jdbi): PackageMetricsDao {
        return jdbi.onDemand(PackageMetricsDao::class.java)
    }

    @Bean
    fun moduleMetricsDao(jdbi: Jdbi): ModuleMetricsDao {
        return jdbi.onDemand(ModuleMetricsDao::class.java)
    }
}

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"))
    runApplication<Application>(*args)
}

package com.thoughtworks.archguard.module.infrastructure.metrics

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.web.WebAppConfiguration
import kotlin.test.assertEquals

@SpringBootTest
@WebAppConfiguration
internal class MetricsRepositoryImplTest {
    @Autowired
    private lateinit var metricsRepositoryImpl: MetricsRepositoryImpl

    @Test
    @Sql("classpath:sqls/insert_metrics.sql")
    internal fun `should get all metrics from metrics table`() {
        val moduleNames = listOf("dubbo-serialization-protobuf", "dubbo-null-test")
        val result = metricsRepositoryImpl.findAllMetrics(moduleNames)

        assertEquals(1, result.size)
        assertEquals(2, result[0].packageMetrics.size)
        assertEquals(4, result[0].packageMetrics[0].classMetrics.size)
    }

    @Test
    @Sql("classpath:sqls/insert_metrics.sql")
    internal fun `should get metrics from metrics table when name is null`() {
        val moduleNames = listOf("dubbo-null-test")
        val result = metricsRepositoryImpl.findAllMetrics(moduleNames)

        assertEquals(0, result.size)
    }

    @Test
    @Sql("classpath:sqls/insert_metrics.sql")
    internal fun `should get module metrics from metrics table`() {
        val moduleNames = listOf("dubbo-serialization-protobuf", "dubbo-null-test")
        val result = metricsRepositoryImpl.findModuleMetrics(moduleNames)

        assertEquals(1, result.size)
        assertEquals(0, result[0].packageMetrics.size)
    }
}
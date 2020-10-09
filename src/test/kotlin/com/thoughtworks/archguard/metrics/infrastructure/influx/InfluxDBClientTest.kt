package com.thoughtworks.archguard.metrics.infrastructure.influx

import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.SpyK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InfluxDBClientTest {

    @SpyK
    private lateinit var influxDBClient: InfluxDBClient

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        influxDBClient = InfluxDBClient("URL")
    }

    @Test
    internal fun should_generate_request_body_when_save_report() {
        val report1: Map<BadSmellType, Long> = mapOf((BadSmellType.SHOTGUN_SURGERY to 1L))
        val report2: Map<BadSmellType, Long> = mapOf((BadSmellType.DATA_CLASS to 2L))
        influxDBClient.saveReport("reportName", "1", report1, report2)

        verify { influxDBClient.save("reportName,system_id=1 SHOTGUN_SURGERY=1,DATA_CLASS=2") }

    }
}
package com.thoughtworks.archguard.metrics.infrastructure.influx

import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class InfluxDBClientTest {

    lateinit var influxDBClient: InfluxDBClient

    @Test
    fun mapToString() {
        influxDBClient = InfluxDBClient("URL")

        val report1: Map<BadSmellType, Long> = mapOf((BadSmellType.DATA_CLASS to 1L))
        val report2: Map<BadSmellType, Long> = mapOf((BadSmellType.SHOTGUN_SURGERY to 2L))
        val result = influxDBClient.mapToString(report1.plus(report2))
        Assertions.assertEquals("DATA_CLASS=1,SHOTGUN_SURGERY=2", result)
    }
}
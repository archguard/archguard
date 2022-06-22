package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.ScaModelDto
import com.thoughtworks.archguard.metrics.infrastructure.influx.InfluxDBClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class InsightDto(
    val systemId: Long,
    val expression: String,
)

data class CustomInsight (
    val systemId: Long,
    val name: String? = "Default",
    val expression: String,
)

@RestController
@RequestMapping("/api/insights")
class InsightController(val insightService: InsightService, val influxDBClient: InfluxDBClient) {
    // 1. query by expression with cron config
    // 2. count after DSL with kotlin scripting ?
    // 3. save by expression and to influx db
    @PostMapping("/sca")
    fun demoSca(@RequestBody insight: InsightDto): List<ScaModelDto> {
        return insightService.byScaArtifact(insight.systemId, insight.expression)
    }

    @PostMapping("/custom")
    fun customInsight(@RequestBody insight: CustomInsight): Int {
        val dtos = insightService.byScaArtifact(insight.systemId, insight.expression)
        val size = dtos.size
        influxDBClient.save("insight,system=${insight.systemId} value=$size")
        return size
    }
}

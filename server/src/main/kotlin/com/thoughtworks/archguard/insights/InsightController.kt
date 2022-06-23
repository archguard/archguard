package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.ScaModelDto
import com.thoughtworks.archguard.metrics.infrastructure.influx.InfluxDBClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.math.roundToInt

data class InsightDto(
    val systemId: Long,
    val expression: String,
)

data class CustomInsight (
    val systemId: Long,
    val name: String? = "Default",
    val expression: String,
)

data class InsightData(val date: String, val name: String?, val value: Int)

@RestController
@RequestMapping("/api/insights")
class InsightController(val insightService: InsightService, val influxDBClient: InfluxDBClient) {
    @PostMapping("/sca")
    fun demoSca(@RequestBody insight: InsightDto): List<ScaModelDto> {
        return insightService.byScaArtifact(insight.systemId, insight.expression)
    }

    @PostMapping("/custom")
    fun customInsight(@RequestBody insight: CustomInsight): Int {
        val dtos = insightService.byScaArtifact(insight.systemId, insight.expression)
        val size = dtos.size
        // todo: add save insight to db
        influxDBClient.save("insight,name=${insight.name},system=${insight.systemId} value=$size")
        return size
    }

    private val TIME: String = "1d"

    @GetMapping("/custom")
    fun listInsights(): List<InsightData> {
        val query = "SELECT * " +
                "FROM \"insight\" "
//               + "GROUP BY time($TIME) fill(none)"

        val graphData = influxDBClient.query(query).map { it.values }
            .flatten().map {
                InsightData(
                    it[0],
                    it[1],
                    it[2].toDouble().roundToInt()
                )
            }

        return graphData
    }
}

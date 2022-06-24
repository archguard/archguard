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

data class InsightData(val date: String, val name: String?, val value: Int)

@RestController
@RequestMapping("/api/insights")
class InsightController(val insightService: InsightService, val repository: InsightRepository, val influxDBClient: InfluxDBClient) {
    @PostMapping("/sca")
    fun demoSca(@RequestBody insight: InsightDto): List<ScaModelDto> {
        return insightService.byScaArtifact(insight.systemId, insight.expression)
    }

    @PostMapping("/custom-insight")
    fun customInsight(@RequestBody insight: CustomInsight): Int {
        repository.saveInsight(insight)
        val dtos = insightService.byScaArtifact(insight.systemId, insight.expression)
        val size = dtos.size
        influxDBClient.save("insight,name=${insight.name},system=${insight.systemId} value=$size")
        return size
    }

    @GetMapping("/custom-insight")
    fun getByName(@RequestBody name: String): CustomInsight {
        return repository.getInsightByName(name)
    }

    @GetMapping("/")
    fun listInsights(): List<InsightData> {
        val query = "SELECT * " +
                "FROM \"insight\" "
//               + "GROUP BY time($TIME) fill(none)"

        val graphData = influxDBClient.query(query).map { it.values }
            .flatten().map {
                InsightData(
                    it[0],
                    it[1],
                    it[3].toDouble().roundToInt()
                )
            }

        return graphData
    }
}

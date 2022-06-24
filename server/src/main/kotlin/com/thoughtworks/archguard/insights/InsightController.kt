package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.ScaModelDto
import com.thoughtworks.archguard.metrics.infrastructure.influx.InfluxDBClient
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import kotlin.math.roundToInt

data class InsightDto(
    val systemId: Long,
    val expression: String,
)

data class InsightData(val date: String, val name: String?, val value: Int)

@RestController
@RequestMapping("/api/insights")
class InsightController(
    val insightApplicationService: InsightApplicationService,
    val repository: InsightRepository,
    val influxDBClient: InfluxDBClient,
) {
    @PostMapping("/sca")
    fun demoSca(@RequestBody insight: InsightDto): List<ScaModelDto> {
        return insightApplicationService.byScaArtifact(insight.systemId, insight.expression)
    }

    @PostMapping("/custom-insight")
    fun customInsight(@RequestBody insight: CustomInsight): Int {
        val insightByName = repository.getInsightByName(insight.name!!)
        if (insightByName == null) {
            repository.saveInsight(insight)
        }

        val dtos = insightApplicationService.byScaArtifact(insight.systemId, insight.expression)
        val size = dtos.size

        influxDBClient.save("insight,name=${insight.name},system=${insight.systemId} value=$size")
        return size
    }

    @GetMapping("/custom-insight/{name}")
    fun getByName(@PathVariable("name") name: String): CustomInsight {
        return repository.getInsightByName(name) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "insight not found: $name"
        )
    }

    @DeleteMapping("/custom-insight/{name}")
    fun deleteByName(@PathVariable("name") name: String): Int {
        val deleteInsightByName = repository.deleteInsightByName(name)
        influxDBClient.query("""DELETE FROM "insight" WHERE "name" = '$name'""")

        return deleteInsightByName
    }

    @GetMapping("/")
    fun listInsights(): List<InsightData> {
        val query = """SELECT * FROM "insight""""

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

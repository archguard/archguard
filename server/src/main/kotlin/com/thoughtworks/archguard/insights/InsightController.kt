package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.application.InsightApplicationService
import com.thoughtworks.archguard.insights.application.InsightModel
import com.thoughtworks.archguard.insights.domain.CustomInsight
import com.thoughtworks.archguard.insights.domain.InsightData
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

data class InsightDto(
    val systemId: Long,
    val expression: String,
    val type: String,
)

@RestController
@RequestMapping("/api/insights")
class InsightController(
    val appService: InsightApplicationService,
    val repository: InsightRepository,
    val influxDBClient: InfluxDBClient,
) {
    @PostMapping("/snapshot")
    fun demoSca(@RequestBody insight: InsightDto): InsightModel {
        return appService.byExpression(insight.systemId, insight.expression, insight.type)
    }

    @PostMapping("/custom-insight")
    fun customInsight(@RequestBody insight: CustomInsight): Int {
        val insightByName = repository.getInsightByName(insight.name!!)
        if (insightByName == null) {
            repository.saveInsight(insight)
        }

        val dtos = appService.byExpression(insight.systemId, insight.expression, insight.type)
        val size = dtos.size

        val data = InsightData.fromCustomInsight(insight).toInflux(size)
        influxDBClient.save(data)
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
        influxDBClient.query("""DELETE FROM "insights" WHERE "name" = '$name'""")
        return deleteInsightByName
    }

    // TODO: it will cause performance issues. when we subscribed lot of data. But before we move to MongoDB, just keep it.
    @GetMapping("/")
    fun listInsights(): List<InsightData> {
        val query = """SELECT * FROM "insights""""

        return influxDBClient.query(query)
            .map { it.values }
            .flatten()
            .map { InsightData.fromInflux(it) }
    }
}

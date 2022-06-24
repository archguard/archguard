package com.thoughtworks.archguard.metrics.infrastructure.influx

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate

@Component
class InfluxDBClient(@Value("\${influxdb.url}") val url: String) {

    private val log = LoggerFactory.getLogger(InfluxDBClient::class.java)

    fun save(requestBody: String) {
        try {
            RestTemplate().postForObject("$url/api/v2/write?bucket=db0&precision=s", requestBody, Void::class.java)
            log.info("save metrics to InfluxDB")
        } catch (sx: HttpServerErrorException) {
            log.error("Server exception when send metrics to InfluxDB. {}", sx)
        } catch (cx: HttpClientErrorException) {
            log.error("Client exception when send metrics to InfluxDB. {}", cx)
        } catch (ex: Exception) {
            log.error("Exception when send metrics to InfluxDB. {}", ex)
        }
    }

    fun saveReport(reportName: String, systemId: String, reports: Map<BadSmellType, Long>) {
        val stringReports = mapToString(reports)
        val requestBody = "$reportName,system_id=$systemId $stringReports"
        save(requestBody)
    }

    fun mapToString(report: Map<BadSmellType, Long>): String {
        return report.map { "${it.key}=${it.value}" }.joinToString(",").trim()
    }

    fun query(query: String): List<InfluxDBSeries> {
        val results = RestTemplate().getForEntity("$url/query?q=$query&db=db0", InfluxDBResponse::class.java).body?.results
        log.info("query results: $results")
        return results?.map { it.series.orEmpty() }?.flatten().orEmpty()
    }
}

data class InfluxDBResponse(val results: List<InfluxDBResult>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InfluxDBResult(val series: List<InfluxDBSeries>?)

data class InfluxDBSeries(val name: String, val columns: List<String>, val values: List<List<String>>)

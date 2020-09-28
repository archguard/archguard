package com.thoughtworks.archguard.metrics.infrastructure.influx

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class InfluxDBClient(@Value("\${influxdb.url}") val url: String) {

    private val log = LoggerFactory.getLogger(InfluxDBClient::class.java)

    fun save(requestBody: String) {
        RestTemplate().postForObject("$url/api/v2/write?bucket=db0&precision=s", requestBody, Void::class.java)
        log.info("save metrics to InfluxDB")
    }

    fun query(query: String): List<InfluxDBSeries> {
        val results = RestTemplate().getForEntity("$url/query?q=${query}&db=db0", InfluxDBResponse::class.java).body?.results
        return results?.map { it.series }?.flatten().orEmpty()
    }
}

data class InfluxDBResponse(val results: List<InfluxDBResult>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InfluxDBResult(val series: List<InfluxDBSeries>)

data class InfluxDBSeries(val name: String, val columns: List<String>, val values: List<List<String>>)


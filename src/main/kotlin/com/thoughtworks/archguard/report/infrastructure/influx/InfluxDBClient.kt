package com.thoughtworks.archguard.report.infrastructure.influx

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class InfluxDBClient(@Value("\${influxdb.url}") val url: String) {

    private val log = LoggerFactory.getLogger(InfluxDBClient::class.java)

    fun save(requestBody: String) {
        RestTemplate().postForObject("$url/api/v2/write?bucket=db0&precision=s", requestBody, Void::class.java)
        log.info("Saved metrics to InfluxDB")
    }
}


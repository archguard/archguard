package com.thoughtworks.archgard.scanner.infrastructure.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class ClassCouplingInfluxDBClient (@Value("\${influxdb.url}") val url: String) {
    private val log = LoggerFactory.getLogger(ClassCouplingInfluxDBClient::class.java)

    fun save(requestBody: String) {
        RestTemplate().postForLocation("$url/api/v2/write?bucket=db0&precision=s", String::class.java, requestBody)
        log.info("save class coupling to InfluxDB")
    }
}


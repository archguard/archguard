package com.thoughtworks.archguard.metrics.infrastructure

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class ClassCouplingInfluxDBClient {
    
    private val log = LoggerFactory.getLogger(ClassCouplingInfluxDBClient::class.java)

    fun save(requestBody: String) {
        val url = "http://influxdb:8086"
        RestTemplate().postForLocation("$url/api/v2/write?bucket=db0&precision=s", String::class.java, requestBody)
        log.info("save class coupling to InfluxDB")
    }
}


package com.thoughtworks.archgard.scanner.infrastructure.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class AnalysisModuleClient (@Value("\${module.client.host}") val url: String) {
    private val log = LoggerFactory.getLogger(AnalysisModuleClient::class.java)

    fun calculateCoupling() {
        RestTemplate().postForLocation("$url/logic-modules/calculate-coupling", null)
        log.info("send calculate coupling request to module service")
    }

}

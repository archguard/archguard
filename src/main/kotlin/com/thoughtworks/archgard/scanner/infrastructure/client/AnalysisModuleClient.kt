package com.thoughtworks.archgard.scanner.infrastructure.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class AnalysisModuleClient (@Value("\${module.client.host}") val url: String) {
    fun calculateCoupling() {
        RestTemplate().postForLocation("$url/logic-modules/calculate-coupling", null)
    }
}
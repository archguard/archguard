package com.thoughtworks.archguard.scanner.infrastructure.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class EvaluationReportClient(@Value("\${evaluation.client.host}") val baseUrl: String) {

    fun generate(type: String) {
        when (EvaluationType.valueOf(type)) {
            EvaluationType.QUALITY -> RestTemplate().postForObject("$baseUrl/quality-evaluations", null, String::class.java)
        }
    }

}

enum class EvaluationType {
    QUALITY
}
package com.thoughtworks.archgard.scanner.infrastructure.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class GitReportClient(@Value("\${evaluation.client.host}") private val baseUrl: String) {

    fun analyse() {
        RestTemplate().postForObject("$baseUrl//analyze", null, String::class.java)
    }

}
package com.thoughtworks.archguard.module.domain.springcloud.httprequest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMethod

class HttpRequestArg(var path: List<String> = mutableListOf(), var method: List<String> = mutableListOf()) {
    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(HttpRequestArg::class.java)

    constructor(values: Map<String, String>) : this() {
        path = objectMapper.readValue(values.getOrDefault("value", ""))
        val temp: List<List<String>> = objectMapper.readValue(values.getOrDefault("method", objectMapper.writeValueAsString(listOf(listOf("", RequestMethod.GET.name)))))
        method = temp.map { it[1] }
    }
}

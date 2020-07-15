package com.thoughtworks.archguard.module.domain.springcloud.httprequest

import com.thoughtworks.archguard.module.common.JsonUtils
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMethod

class HttpRequestArg(var path: List<String> = mutableListOf(), var method: List<String> = mutableListOf()) {
    private val log = LoggerFactory.getLogger(HttpRequestArg::class.java)

    constructor(values: Map<String, String>) : this() {
        path = JsonUtils.json2obj(values.getOrDefault("value", JsonUtils.obj2json("")))
        val temp: List<List<String>> = JsonUtils.json2obj(values.getOrDefault("method", JsonUtils.obj2json(listOf(listOf("", RequestMethod.GET.name)))))
        method = temp.map { it[1] }
    }
}

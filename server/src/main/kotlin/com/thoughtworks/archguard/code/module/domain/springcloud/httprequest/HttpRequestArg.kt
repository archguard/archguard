package com.thoughtworks.archguard.code.module.domain.springcloud.httprequest

import com.thoughtworks.archguard.common.JsonUtils
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMethod

class HttpRequestArg(var paths: List<String> = mutableListOf(), var methods: List<String> = mutableListOf()) {
    private val log = LoggerFactory.getLogger(HttpRequestArg::class.java)

    constructor(values: Map<String, String>) : this() {
        paths = JsonUtils.json2obj(values.getOrDefault("value", JsonUtils.obj2json("")))
        val temp: List<List<String>> = JsonUtils.json2obj(values.getOrDefault("method", JsonUtils.obj2json(listOf(listOf("", RequestMethod.GET.name)))))
        methods = temp.map { it[1] }
    }
}

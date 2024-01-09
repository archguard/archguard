package org.archguard.protocol.http

import org.archguard.json.JsonUtils
import org.springframework.web.bind.annotation.RequestMethod

class HttpRequestArg(var paths: List<String> = mutableListOf(), var methods: List<String> = mutableListOf()) {
    constructor(values: Map<String, String>) : this() {
        paths = JsonUtils.json2obj(values.getOrDefault("value", JsonUtils.obj2json("")))
        val temp: List<List<String>> = JsonUtils.json2obj(values.getOrDefault("method", JsonUtils.obj2json(listOf(listOf("", RequestMethod.GET.name)))))
        methods = temp.map { it[1] }
    }
}

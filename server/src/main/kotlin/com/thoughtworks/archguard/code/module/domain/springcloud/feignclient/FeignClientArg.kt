package com.thoughtworks.archguard.code.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.common.JsonUtils

class FeignClientArg(values: Map<String, String>) {
    val name: String = JsonUtils.json2obj(values.getOrDefault("name", JsonUtils.obj2json("")))
    val path: String = JsonUtils.json2obj(values.getOrDefault("path", JsonUtils.obj2json("")))
}

package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.module.common.JsonUtils

class FeignClientArg(values: Map<String, String>) {
    val name: String = JsonUtils.json2obj(values.getOrDefault("name", ""))

}

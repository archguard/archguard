package org.archguard.protocol.feigh

import org.archguard.json.JsonUtils

class FeignClientArg(values: Map<String, String>) {
    val name: String = JsonUtils.json2obj(values.getOrDefault("name", JsonUtils.obj2json("")))
    val path: String = JsonUtils.json2obj(values.getOrDefault("path", JsonUtils.obj2json("")))
}

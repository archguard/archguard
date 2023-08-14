package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanUrl {
    var raw: String? = null
    var host: List<String>? = null
    var port: String? = null
    var path: List<String>? = null
    var query: List<PostmanQuery>? = null
    var variable: List<PostmanVariable>? = null
    var protocol: String? = null
}

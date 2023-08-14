package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanBody {
    var mode: String? = null
    var file: PostmanFile? = null
    var raw: String? = null
    var urlencoded: List<PostmanUrlEncoded>? = null
    var formdata: List<PostmanFormData>? = null
    var options: PostmanOptions? = null
}

@Serializable
class PostmanOptions {
    var raw: PostmanRaw? = null
}

@Serializable
class PostmanRaw {
    var language: String? = null
}

@Serializable
class PostmanFormData {
    var key: String? = null
    var value: String? = null
    var type: String? = null
    var disabled: Boolean? = null
    var description: String? = null
}

package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanQuery {
    var key: String? = null
    var value: String? = null
    var description: String? = null
    var disabled: Boolean? = null
}

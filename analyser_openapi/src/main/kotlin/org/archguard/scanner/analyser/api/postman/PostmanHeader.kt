package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanHeader {
    var key: String? = null
    var value: String? = null
    var type: String? = null
    var description: String? = null
    var disabled: Boolean? = null
}

package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanEvent {
    var listen: String? = null
    var script: PostmanScript? = null
}

package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanProtocolProfileBehavior {
    var disableBodyPruning: Boolean? = null
}
package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable

@Serializable
open class PostmanItem {
    open val item: List<PostmanItem>? = null
    open var name: String? = null
    open var description: String? = null
    open var event: List<PostmanEvent>? = null
    open var request: PostmanRequest? = null
    open var response: List<PostmanResponse>? = null
    open var protocolProfileBehavior: PostmanProtocolProfileBehavior? = null
}


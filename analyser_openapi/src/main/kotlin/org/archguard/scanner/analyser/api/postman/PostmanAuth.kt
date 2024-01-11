package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanAuth {
    // auth type: "oauth2",
    var type: String? = null
    var bearer: List<PostmanVariable>? = null
    var oauth2: List<PostmanVariable>? = null

    fun format(): String {
        if (type == "bearer") {
            val string = bearer?.joinToString(",", transform = PostmanVariable::format)
            return "Bearer $string"
        }

        if (type == "oauth2") {
            val string = oauth2?.joinToString(",", transform = PostmanVariable::format)
            return "OAuth2 $string"
        }

        return "type $type"
    }
}

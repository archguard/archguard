package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanAuth {
    // auth type: "oauth2",
    var type: String? = null
    var bearer: List<org.archguard.scanner.analyser.api.postman.PostmanVariable>? = null
    var oauth2: List<org.archguard.scanner.analyser.api.postman.PostmanVariable>? = null

    fun format(): String {
        if (type == "bearer") {
            val string = bearer?.joinToString(",", transform = org.archguard.scanner.analyser.api.postman.PostmanVariable::format)
            return "Bearer $string"
        }

        if (type == "oauth2") {
            val string = oauth2?.joinToString(",", transform = org.archguard.scanner.analyser.api.postman.PostmanVariable::format)
            return "OAuth2 $string"
        }

        return "type $type"
    }
}

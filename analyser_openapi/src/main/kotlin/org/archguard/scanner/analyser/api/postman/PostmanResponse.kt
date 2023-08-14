package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanResponse(
    val _postman_previewlanguage: String? = null,
    val name: String? = null,
    val originalRequest: PostmanRequest? = null,
    val status: String? = null,
    val code: Int? = null,
    val header: List<PostmanHeader>? = null,
    val cookie: List<String>? = null,
    val body: String? = null,
    val urlencoded: List<PostmanUrlEncoded>? = null,
//    val data: List<Data>,
//    val links: Links,
//    val meta: Meta
)

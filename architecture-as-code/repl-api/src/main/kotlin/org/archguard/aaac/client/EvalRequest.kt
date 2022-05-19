package org.archguard.aaac.client

import kotlinx.serialization.Serializable

@Serializable
data class EvalRequest(
    var id: Int = -1,
    val code: String,
    // for: post data?
    val serverUrl: String = ""
)

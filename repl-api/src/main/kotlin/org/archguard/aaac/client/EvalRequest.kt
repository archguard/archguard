package org.archguard.aaac.client

import kotlinx.serialization.Serializable

@Serializable
data class EvalRequest(
    val code: String,
    // for: post data?
    val serverUrl: String = ""
)

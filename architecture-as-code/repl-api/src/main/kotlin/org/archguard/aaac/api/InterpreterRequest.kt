package org.archguard.aaac.api

import kotlinx.serialization.Serializable

@Serializable
data class InterpreterRequest(
    var id: Int = -1,
    val code: String,
    // for: post data?
    val serverUrl: String = ""
)

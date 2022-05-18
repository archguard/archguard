package org.archguard.aaac

import kotlinx.serialization.Serializable

@Serializable
data class CodeEvalContext(
    val code: String,
    // for: post data?
    val serverUrl: String = ""
)

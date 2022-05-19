package org.archguard.aaac.model

import kotlinx.serialization.Serializable
import org.archguard.dsl.Action

@Serializable
data class ReplResult(
    var resultValue: String,
    var isArchdocApi: Boolean = false,
    var className: String = "",
    var actionData: String = "",
    var action: Action? = null
)
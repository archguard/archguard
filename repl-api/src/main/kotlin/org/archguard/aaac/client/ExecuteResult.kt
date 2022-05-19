package org.archguard.aaac.client

import kotlinx.serialization.Serializable
import org.archguard.dsl.Action

@Serializable
data class ExecuteResult(
    var resultValue: String,
    var className: String = "",

    // to frontend
    var actionData: String = "",
    var actionType: String = "",
    var action: Action? = null
)
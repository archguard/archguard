package org.archguard.aaac.client

import kotlinx.serialization.Serializable
import org.archguard.dsl.Action

@Serializable
data class ExecuteResult(
    var resultValue: String,
    var className: String = "",

    // for success
    var isArchGuardAaac: Boolean = false,
    var actionData: String = "",
    var actionType: String = "",
    var action: Action? = null

    // for error?
)

package org.archguard.aaac.client

import kotlinx.serialization.Serializable
import org.archguard.dsl.Action

@Serializable
data class ExecuteResult(
    var resultValue: String,
    var className: String = "",
    var isAaacCode: Boolean = false,

    var actionData: String = "",
    var actionType: String = "",
    var action: Action? = null
)
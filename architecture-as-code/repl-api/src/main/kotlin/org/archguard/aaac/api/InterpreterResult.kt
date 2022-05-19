package org.archguard.aaac.api

import kotlinx.serialization.Serializable
import org.archguard.dsl.model.ReactiveAction

@Serializable
data class InterpreterResult(
    var resultValue: String,
    var className: String = "",

    // for success
    var isArchGuardAaac: Boolean = false,
    var actionData: String = "",
    var actionType: String = "",
    var action: ReactiveAction? = null

    // for error?
)

package org.archguard.aaac.api.messaging

import kotlinx.serialization.Serializable
import org.archguard.dsl.model.ReactiveAction

@Serializable
data class Message(
    var resultValue: String,
    var className: String = "",
    var msgType: AaacMessageType = AaacMessageType.NONE,

    var action: ReactiveAction? = null
)

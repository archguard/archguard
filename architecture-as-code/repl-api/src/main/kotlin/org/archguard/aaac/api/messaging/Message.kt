package org.archguard.aaac.api.messaging

import kotlinx.serialization.Serializable
import org.archguard.dsl.base.model.ReactiveAction

@Serializable
data class Message(
    var id: Int = -1,
    var resultValue: String,
    var className: String = "",
    var msgType: MessageType = MessageType.NONE,
    var content: MessageContent? = null,
    var action: ReactiveAction? = null,
)

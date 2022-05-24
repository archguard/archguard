package com.thoughtworks.archguard.aac.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AsCodeContent


@kotlinx.serialization.Serializable
class RepoStatus(val success: List<String>, val failure: List<String>) : AsCodeContent() {}

@Serializable
enum class ServerMessageType {
    @SerialName("normal")
    NORMAL,
}

@Serializable
class AsCodeResponse(
    var msgType: ServerMessageType = ServerMessageType.NORMAL,
    var content: AsCodeContent,
) {

}

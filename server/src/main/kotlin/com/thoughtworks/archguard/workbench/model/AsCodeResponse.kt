package com.thoughtworks.archguard.workbench.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AsCodeContent

@Serializable
class RepoStatus(val success: List<String>, val existed: List<String>) : AsCodeContent()

@Serializable
class PlaceHolder() : AsCodeContent()

@Serializable
enum class ServerMessageType {
    @SerialName("normal")
    NORMAL,
}

@Serializable
class AsCodeResponse(
    var msgType: ServerMessageType = ServerMessageType.NORMAL,
    var content: AsCodeContent,
)

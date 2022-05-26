package org.archguard.dsl.base.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ActionType {
    @SerialName("graph")
    GRAPH,

    @SerialName("create_repo")
    CREATE_REPOS,

    @SerialName("create_scan")
    CREATE_SCAN
}
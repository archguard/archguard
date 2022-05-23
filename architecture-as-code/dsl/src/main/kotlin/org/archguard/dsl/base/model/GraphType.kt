package org.archguard.dsl.base.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GraphType {
    @SerialName("archdoc")
    ARCHDOC,

    @SerialName("uml")
    UML,
}
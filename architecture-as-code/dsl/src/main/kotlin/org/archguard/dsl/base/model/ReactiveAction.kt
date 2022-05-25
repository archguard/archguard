package org.archguard.dsl.base.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class ReactiveAction(
    val actionType: ActionType,
    val className: String,
    val graphType: GraphType,
    val data: String
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}
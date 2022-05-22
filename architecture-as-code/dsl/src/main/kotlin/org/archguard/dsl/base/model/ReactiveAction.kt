package org.archguard.dsl.base.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class ReactiveAction(
    val actionType: String,
    val className: String,
    val graphType: String,
    val data: String
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}
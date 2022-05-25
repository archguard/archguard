package org.archguard.dsl.base.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class LayeredRelation(val source: String, val target: String) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}
package org.archguard.dsl.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.dsl.Element

@Serializable
class Repository(val name: String, val language: String, val scmUrl: String) : Element {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}
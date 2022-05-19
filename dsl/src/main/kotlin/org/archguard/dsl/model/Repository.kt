package org.archguard.dsl.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// todo: align to backend API

@Serializable
class RepositoryAuth (val name: String, val password: String) {}

@Serializable
class Repository(val name: String, val language: String, val scmUrl: String) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}
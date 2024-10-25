package org.archguard.context

import kotlinx.serialization.Serializable

@Serializable
data class CompositionDependency(
    val id: String,
    val name: String,
    val path: String,
    val version: String,
    val parentId: String,
    val packageManager: String,
    val depName: String,
    val depGroup: String,
    val depArtifact: String,
    val depMetadata: String = "",
    val depSource: String = "",
    val depScope: String,
    val depVersion: String
)

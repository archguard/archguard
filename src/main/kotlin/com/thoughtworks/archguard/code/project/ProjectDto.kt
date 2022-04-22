package com.thoughtworks.archguard.code.project

data class CompositionDependency(
    val id: String = "",
    val systemId: String = "",
    val name: String = "",
    val version: String = "",
    val parentId: String = "",
    val packageManager: String = "",
    val depName: String = "",
    val depGroup: String = "",
    val depArtifact: String = "",
    val depMetadata: String = "",
    val depSource: String = "",
    val depScope: String = ""
)

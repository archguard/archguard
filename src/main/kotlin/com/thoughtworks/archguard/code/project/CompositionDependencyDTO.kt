package com.thoughtworks.archguard.code.project

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

class CompositionDependencyDTO {
    val id: String
    val systemId: String
    val name: String
    val version: String
    val parentId: String
    val packageManager: String
    val depName: String
    val depGroup: String
    val depArtifact: String
    val depMetadata: String
    val depSource: String
    val depScope: String

    // JDBI issue?
    @JdbiConstructor
    constructor(
        id: String = "",
        systemId: String = "",
        name: String = "",
        version: String = "",
        parentId: String = "",
        packageManager: String = "",
        depName: String = "",
        depGroup: String = "",
        depArtifact: String = "",
        depMetadata: String = "",
        depSource: String = "",
        depScope: String = ""
    ) {
        this.id = id
        this.systemId = systemId
        this.name = name
        this.version = version
        this.parentId = parentId
        this.packageManager = packageManager
        this.depName = depName
        this.depGroup = depGroup
        this.depArtifact = depArtifact
        this.depMetadata = depMetadata
        this.depSource = depSource
        this.depScope = depScope
    }
}

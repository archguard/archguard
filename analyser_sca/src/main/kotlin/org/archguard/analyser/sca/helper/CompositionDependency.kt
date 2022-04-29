package org.archguard.analyser.sca.helper

@Sql("project_composition_dependencies")
data class CompositionDependency(
    @Sql("id") val id: String,
    @Sql("system_id") val systemId: String,
    @Sql("name") val name: String,
    @Sql("version") val version: String,
    @Sql("parent_id") val parentId: String,
    @Sql("package_manager") val packageManager: String,
    @Sql("dep_name") val depName: String,
    @Sql("dep_group") val depGroup: String,
    @Sql("dep_artifact") val depArtifact: String,
    @Sql("dep_metadata") val depMetadata: String = "",
    @Sql("dep_source") val depSource: String = "",
    @Sql("dep_scope") val depScope: String,
    @Sql("dep_version") val depVersion: String
)
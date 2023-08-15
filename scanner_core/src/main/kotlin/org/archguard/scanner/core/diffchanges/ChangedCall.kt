package org.archguard.scanner.core.diffchanges

import kotlinx.serialization.Serializable

@Serializable
data class ChangedCall(
    val path: String,
    val packageName: String,
    val className: String,
    val relations: List<NodeRelation>
)

@Serializable
data class NodeRelation(
    val source: String,
    val target: String
)

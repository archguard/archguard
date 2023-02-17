package org.archguard.scanner.core.diffchanges

import kotlinx.serialization.Serializable

@Serializable
data class ChangedCall(
    val path: String,
    val packageName: String,
    val className: String,
    val relations: List<ChangeRelation>
)

@Serializable
data class ChangeRelation(
    val source: String,
    val target: String
)

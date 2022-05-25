package org.archguard.scanner.core.diffchanges

import kotlinx.serialization.Serializable

@Serializable
class ChangedCall(
    val path: String,
    val packageName: String,
    val className: String,
    val relations: List<ChangeRelation>
)

@Serializable
class ChangeRelation(
    val source: String,
    val target: String
)

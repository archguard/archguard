package org.archguard.scanner.core.client.dto

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

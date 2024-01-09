package org.archguard.architecture.view.concept

import chapi.domain.core.CodeDataStruct

/**
 * should be tree structure
 */
class DomainModel(
    val name: String,
    val fields: List<String>,
    val behaviors: List<String>,
) {
    companion object {
        fun from(concepts: List<CodeDataStruct>): List<DomainModel> {
            return concepts.map { it ->
                DomainModel(
                    name = it.NodeName,
                    fields = it.Fields.map { it.TypeKey },
                    behaviors = it.Functions.map { it.Name }
                )
            }
        }
    }
}

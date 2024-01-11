package org.archguard.architecture.view.concept

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable

/**
 * should be tree structure
 */
@Serializable
class DomainModel(
    val name: String,
    val fields: List<String>,
    val behaviors: List<String>,
) {
    companion object {
        fun from(concepts: List<CodeDataStruct>): List<DomainModel> {
            return concepts.map { ds ->
                DomainModel(
                    name = ds.getClassFullName(),
                    fields = ds.Fields.map { it.TypeKey },
                    behaviors = ds.Functions.mapNotNull {
                        val notConstructor = it.Name != ds.NodeName
                        val noOverride = it.Name != "toString" && it.Name != "equals" && it.Name != "hashCode"
                        if (noOverride && notConstructor) {
                            it.Name
                        } else {
                            null
                        }
                    }
                )
            }
        }
    }
}

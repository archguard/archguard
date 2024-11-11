package org.archguard.architecture.view.concept

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import org.archguard.architecture.tokenizer.CodeNamingTokenizer

@Serializable
data class DomainMeta(
    val path: String,
    val relativePath: String,
)

/**
 * should be tree structure
 */
@Serializable
class DomainModel(
    val name: String,
    val fields: List<String>,
    val behaviors: List<String>,
    val meta: DomainMeta? = null,
    val dict: List<String>,
) {
    companion object {
        fun from(concepts: List<CodeDataStruct>, workspace: String): List<DomainModel> {
            val tokenizer = CodeNamingTokenizer()

            return concepts.map { ds ->
                val relativePath = try {
                    ds.FilePath.substring(workspace.length).removePrefix("/")
                } catch (e: Exception) {
                    ds.FilePath
                }

                DomainModel(
                    name = ds.getClassFullName(),
                    fields = ds.Fields.map { it.TypeKey },
                    meta = DomainMeta(
                        path = ds.FilePath,
                        relativePath = relativePath
                    ),
                    behaviors = ds.Functions.mapNotNull {
                        val notConstructor = it.Name != ds.NodeName
                        val noOverride = it.Name != "toString" && it.Name != "equals" && it.Name != "hashCode"
                        if (noOverride && notConstructor) {
                            it.Name
                        } else {
                            null
                        }
                    },
                    dict = tokenizer.tokenize(ds.NodeName) + ds.Functions.flatMap {
                        tokenizer.tokenize(it.Name)
                    }
                )
            }
        }
    }
}

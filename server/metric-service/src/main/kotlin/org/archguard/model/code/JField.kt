package org.archguard.model.code

import kotlinx.serialization.Serializable
import org.archguard.graph.Node
import org.jdbi.v3.core.mapper.reflect.ColumnName
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

@Serializable
data class JField
@JdbiConstructor
constructor(
    @ColumnName("id") val id: String,
    @ColumnName("name") val name: String,
    @ColumnName("type") val type: String
) : Node {
    override fun getNodeId(): String {
        return id
    }
}

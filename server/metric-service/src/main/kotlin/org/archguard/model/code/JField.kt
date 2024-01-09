package org.archguard.model.code

import org.archguard.graph.Node
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class JField(
    @ColumnName("id") val id: String,
    @ColumnName("name") val name: String,
    @ColumnName("type") val type: String) : Node {
    override fun getNodeId(): String {
        return id
    }
}

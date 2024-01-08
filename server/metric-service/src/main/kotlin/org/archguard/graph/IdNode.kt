package org.archguard.graph

data class IdNode(val id: String) : Node {
    override fun getNodeId(): String {
        return id
    }
}

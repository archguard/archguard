package com.thoughtworks.archguard.scanner2.domain.model

import org.archguard.graph.Node

data class JField(val id: String, val name: String, val type: String) : Node {
    override fun getNodeId(): String {
        return id
    }
}

package com.thoughtworks.archguard.scanner2.domain.model

import org.archguard.graph.Node

data class IdNode(val id: String) : Node {
    override fun getNodeId(): String {
        return id
    }
}

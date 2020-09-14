package com.thoughtworks.archgard.scanner2.domain.model

data class IdNode(val id: String) : Node {
    override fun getNodeId(): String {
        return id
    }
}
package com.thoughtworks.archgard.scanner2.domain.model

data class JField(val id: String, val name: String, val type: String) : Node {
    override fun getNodeId(): String {
        return id
    }
}

package com.thoughtworks.archguard.code.clazz.domain

import com.thoughtworks.archguard.module.domain.graph.Node

data class JField(val id: String, val name: String, val type: String) : Node {
    override fun getNodeId(): String {
        return id
    }
}

package com.thoughtworks.archguard.v2.frontier.clazz.domain

import com.thoughtworks.archguard.code.module.domain.graph.Node

data class JField(val id: String, val name: String, val type: String) : Node {
    override fun getNodeId(): String {
        return id
    }
}

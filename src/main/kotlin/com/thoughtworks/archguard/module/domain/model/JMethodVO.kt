package com.thoughtworks.archguard.module.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.thoughtworks.archguard.module.domain.graph.Node
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

data class JMethodVO(val name: String, val clazz: JClassVO, val returnType: String, val argumentTypes: List<String>) : Node {
    var id: String? = null
    val fullName = "${clazz.getFullName()}.$name"

    @JdbiConstructor
    constructor(name: String, className: String, moduleName: String, returnType: String, argumentTypes: List<String>) : this(name, JClassVO(className, moduleName), returnType, argumentTypes)

    @JsonIgnore
    override fun getNodeId(): String {
        return id!!
    }
}

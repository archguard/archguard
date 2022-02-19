package com.thoughtworks.archguard.scanner2.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

data class JMethodVO(val name: String, val clazz: JClassVO, val returnType: String, val argumentTypes: List<String>) : Node {
    var id: String? = null
    val fullName = clazz.fullName + "." + name + "(" + argumentTypes.joinToString(separator = ",") + ")"

    @JdbiConstructor
    constructor(name: String, className: String, moduleName: String?, returnType: String, argumentTypes: List<String>) : this(name, JClassVO(className, moduleName), returnType, argumentTypes)

    @JsonIgnore
    override fun getNodeId(): String {
        return id!!
    }
}

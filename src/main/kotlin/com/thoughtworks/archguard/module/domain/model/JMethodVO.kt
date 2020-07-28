package com.thoughtworks.archguard.module.domain.model

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

data class JMethodVO(val name: String, val clazz: JClassVO, val returnType: String, val argumentTypes: List<String>) {
    val fullName = "${clazz.getFullName()}.$name"

    @JdbiConstructor
    constructor(name: String, className: String, moduleName: String, returnType: String, argumentTypes: List<String>) : this(name, JClassVO(className, moduleName), returnType, argumentTypes)

}

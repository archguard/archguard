package com.thoughtworks.archguard.module.domain.model

import com.thoughtworks.archguard.method.domain.JMethod
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

data class JMethodVO(val name: String, val clazz: JClassVO, val returnType: String, val argumentTypes: List<String>) {
    val fullName = "${clazz.getFullName()}.$name"

    @JdbiConstructor
    constructor(name: String, className: String, moduleName: String, returnType: String, argumentTypes: List<String>) : this(name, JClassVO(className, moduleName), returnType, argumentTypes)

    companion object {
        fun fromJMethod(jMethod: JMethod): JMethodVO {
            return JMethodVO(jMethod.name, jMethod.clazz, jMethod.module, jMethod.returnType, jMethod.argumentTypes)
        }
    }
}

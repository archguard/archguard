package com.thoughtworks.archguard.module.domain.model

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

data class JMethodVO(val jClassVO: JClassVO, val name: String) {
    val fullName = "${jClassVO.getFullName()}.$name"

    @JdbiConstructor
    constructor(moduleName: String, className: String, name: String) : this(JClassVO(className, moduleName), name)

}

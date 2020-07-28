package com.thoughtworks.archguard.module.domain.model

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

data class JMethodVO(val name: String, val jClassVO: JClassVO) {
    val fullName = "${jClassVO.getFullName()}.$name"

    @JdbiConstructor
    constructor(name: String, className: String, moduleName: String) : this(name, JClassVO(className, moduleName))

}

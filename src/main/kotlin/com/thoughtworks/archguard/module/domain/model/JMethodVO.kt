package com.thoughtworks.archguard.module.domain.model

data class JMethodVO(val jClassVO: JClassVO, val name: String) {
    val fullName = "${jClassVO.getFullName()}.$name"

    constructor(moduleName: String, className: String, name: String) : this(JClassVO(className, moduleName), name)

}

package com.thoughtworks.archguard.module.domain.model

class JMethod(val name: String, val jClass: JClassVO) {
    lateinit var id: String

    constructor(id: String, name: String, module: String) : this(name, JClassVO(name, module)) {
        this.id = id
    }

}
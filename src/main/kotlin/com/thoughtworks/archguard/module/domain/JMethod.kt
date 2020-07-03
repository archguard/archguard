package com.thoughtworks.archguard.module.domain

class JMethod(val name: String, val jClass: JClass) {
    lateinit var id: String

    constructor(id: String, name: String, module: String) : this(name, JClass(name, module)) {
        this.id = id
    }

}
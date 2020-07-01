package com.thoughtworks.archguard.module.domain

class JClass(val name: String, val module: String) {
    lateinit var id: String

    constructor(id: String, name: String, module: String) : this(name, module) {
        this.id = id
    }
}
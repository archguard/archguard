package com.thoughtworks.archguard.module.domain

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor

class JClass(val name: String, val module: String) {
    lateinit var id: String

    @JdbiConstructor
    constructor(id: String, name: String, module: String) : this(name, module) {
        this.id = id
    }
}
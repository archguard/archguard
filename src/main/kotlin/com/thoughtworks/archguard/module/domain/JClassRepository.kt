package com.thoughtworks.archguard.module.domain

interface JClassRepository {
    fun getJClassBy(name: String, module: String): JClass
}
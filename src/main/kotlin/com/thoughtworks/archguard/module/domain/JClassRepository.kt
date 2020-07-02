package com.thoughtworks.archguard.module.domain

interface JClassRepository {
    fun getJClassByName(name: String): JClass
}
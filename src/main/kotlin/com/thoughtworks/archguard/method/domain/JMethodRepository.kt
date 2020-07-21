package com.thoughtworks.archguard.method.domain

interface JMethodRepository {
    fun findMethodsByModuleAndClass(module: String, name: String): List<JMethod>
}
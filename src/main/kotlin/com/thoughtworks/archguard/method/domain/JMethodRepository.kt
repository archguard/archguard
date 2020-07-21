package com.thoughtworks.archguard.method.domain

interface JMethodRepository {
    fun findMethodsByModuleAndClass(module: String, name: String): List<JMethod>
    fun findMethodCallees(id: String): List<JMethod>
    fun findMethodImplements(id: String, name: String): List<JMethod>
}
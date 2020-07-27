package com.thoughtworks.archguard.method.domain

interface JMethodRepository {
    fun findMethodsByModuleAndClass(module: String, name: String): List<JMethod>
    fun findMethodCallees(id: String): List<JMethod>
    fun findMethodCallers(id: String): List<JMethod>
    fun findMethodImplements(id: String, name: String): List<JMethod>
    fun findMethodByModuleAndClazzAndName(moduleName: String, clazzName: String, methodName: String): List<JMethod>
    fun findMethodByClazzAndName(clazzName: String, methodName: String): List<JMethod>
}
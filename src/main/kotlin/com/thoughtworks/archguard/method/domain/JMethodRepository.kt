package com.thoughtworks.archguard.method.domain

import com.thoughtworks.archguard.clazz.domain.JField

interface JMethodRepository {
    fun findMethodsByModuleAndClass(projectId: Long, module: String, name: String): List<JMethod>
    fun findMethodCallees(id: String): List<JMethod>
    fun findMethodCallers(id: String): List<JMethod>
    fun findMethodFields(id: String): List<JField>
    fun findMethodImplements(id: String, name: String): List<JMethod>
    fun findMethodByModuleAndClazzAndName(projectId: Long, moduleName: String, clazzName: String, methodName: String): List<JMethod>
}

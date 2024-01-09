package com.thoughtworks.archguard.code.method.domain

import org.archguard.model.code.JField

interface JMethodRepository {
    fun findMethodsByModuleAndClass(systemId: Long, module: String?, name: String): List<JMethod>
    fun findMethodCallees(id: String): List<JMethod>
    fun findMethodCallers(id: String): List<JMethod>
    fun findMethodFields(id: String): List<JField>
    fun findMethodImplements(id: String, name: String): List<JMethod>
    fun findMethodByModuleAndClazzAndName(systemId: Long, moduleName: String?, clazzName: String, methodName: String): List<JMethod>
}

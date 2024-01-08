package com.thoughtworks.archguard.scanner2.infrastructure.po

import org.archguard.bytecode.TypeMap
import org.archguard.model.code.JMethod

class JMethodPO(val id: String, val name: String, val clazz: String, val module: String?, val returnType: String, val argumentTypes: String?, val access: String) {
    fun toJMethod(): JMethod {
        val argumentTypeList = if (argumentTypes.isNullOrBlank()) emptyList() else argumentTypes.split(",")
        val jMethod = JMethod(id, name, clazz, module, returnType, argumentTypeList)
        try {
            TypeMap.getMethodType(access.toInt()).forEach { jMethod.addType(it) }
        } catch (e: NumberFormatException) {
            return jMethod
        }
        return jMethod
    }
}

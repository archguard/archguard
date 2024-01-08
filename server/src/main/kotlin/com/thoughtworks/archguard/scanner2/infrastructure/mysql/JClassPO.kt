package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import org.archguard.model.code.JClass

class JClassPO(val id: String, val name: String, val module: String?, val loc: Int?, val access: String?) {
    fun toJClass(): JClass {
        val jClass = JClass(id, name, module)
        if (access == null) {
            return jClass
        }
        val accessInt = access.toIntOrNull()
        if (accessInt != null) {
            TypeMap.getClassType(accessInt).forEach { jClass.addClassType(it) }
        }
        return jClass
    }
}

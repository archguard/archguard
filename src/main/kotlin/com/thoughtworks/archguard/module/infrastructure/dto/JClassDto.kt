package com.thoughtworks.archguard.module.infrastructure.dto

import com.thoughtworks.archguard.module.domain.model.ClazzType
import com.thoughtworks.archguard.module.domain.model.JClass

class JClassDto(val id: String, val name: String, val module: String, val loc: Int?, val access: String?) {
    fun toJClass(): JClass {
        val jClass = JClass(id, name, module)
        if (access == null) {
            return jClass
        }
        val accessInt = access.toIntOrNull()
        if (accessInt != null) {
            jClass.classType = ClassTypeMap.getClassType(accessInt)
        }
        return jClass
    }
}

object ClassTypeMap {
    private const val ACC_INTERFACE = 512

    fun getClassType(access: Int): ClazzType {
        if ((access.and(ACC_INTERFACE)) != 0) {
            return ClazzType.INTERFACE
        }
        // TODO: support more type https://asm.ow2.io/javadoc/constant-values.html#org.objectweb.asm.Opcodes
        return ClazzType.NOT_DEFINED
    }

}
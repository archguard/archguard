package com.thoughtworks.archguard.clazz.infrastructure.dto

import com.thoughtworks.archguard.clazz.domain.ClazzType
import com.thoughtworks.archguard.clazz.domain.JClass

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
    private const val ACC_ABSTRACT = 1024

    fun getClassType(access: Int): List<ClazzType> {
        val classTypes: MutableList<ClazzType> = mutableListOf()
        if ((access.and(ACC_INTERFACE)) != 0) {
            classTypes.add(ClazzType.INTERFACE)
        }
        if ((access.and(ACC_ABSTRACT)) != 0) {
            classTypes.add(ClazzType.ABSTRACT_CLASS)
        }
        // TODO: support more type https://asm.ow2.io/javadoc/constant-values.html#org.objectweb.asm.Opcodes
        return classTypes
    }

}

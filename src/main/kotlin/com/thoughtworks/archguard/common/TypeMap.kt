package com.thoughtworks.archguard.common

import com.thoughtworks.archguard.code.clazz.domain.ClazzType
import com.thoughtworks.archguard.code.method.domain.MethodType

object TypeMap {
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

    fun getMethodType(access: Int): List<MethodType> {
        val methodTypes: MutableList<MethodType> = mutableListOf()
        if ((access.and(ACC_ABSTRACT)) != 0) {
            methodTypes.add(MethodType.ABSTRACT_METHOD)
        }
        // TODO: support more type https://asm.ow2.io/javadoc/constant-values.html#org.objectweb.asm.Opcodes
        return methodTypes
    }

}
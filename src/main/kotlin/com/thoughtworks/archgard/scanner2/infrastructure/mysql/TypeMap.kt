package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.ClazzType
import com.thoughtworks.archgard.scanner2.domain.model.MethodType

object TypeMap {
    private const val ACC_INTERFACE = 512
    private const val ACC_ABSTRACT = 1024
    private const val ACC_SYNTHETIC = 4096

    fun getClassType(access: Int): List<ClazzType> {
        val classTypes: MutableList<ClazzType> = mutableListOf()
        if ((access.and(ACC_INTERFACE)) != 0) {
            classTypes.add(ClazzType.INTERFACE)
        }
        if ((access.and(ACC_ABSTRACT)) != 0) {
            classTypes.add(ClazzType.ABSTRACT_CLASS)
        }
        if ((access.and(ACC_SYNTHETIC)) != 0) {
            classTypes.add(ClazzType.SYNTHETIC)
        }
        // TODO: support more type https://asm.ow2.io/javadoc/constant-values.html#org.objectweb.asm.Opcodes
        return classTypes
    }

    fun getMethodType(access: Int): List<MethodType> {
        val methodTypes: MutableList<MethodType> = mutableListOf()
        if ((access.and(ACC_ABSTRACT)) != 0) {
            methodTypes.add(MethodType.ABSTRACT_METHOD)
        }
        if ((access.and(ACC_SYNTHETIC)) != 0) {
            methodTypes.add(MethodType.SYNTHETIC)
        }
        // TODO: support more type https://asm.ow2.io/javadoc/constant-values.html#org.objectweb.asm.Opcodes
        return methodTypes
    }

}
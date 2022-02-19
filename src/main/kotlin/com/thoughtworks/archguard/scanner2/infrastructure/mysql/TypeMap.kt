package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import com.thoughtworks.archguard.scanner2.domain.model.ClazzType
import com.thoughtworks.archguard.scanner2.domain.model.MethodType

object TypeMap {
    private const val ACC_INTERFACE = 512
    private const val ACC_ABSTRACT = 1024
    private const val ACC_SYNTHETIC = 4096
    private const val ACC_PRIVATE = 2
    private const val ACC_STATIC = 8

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
        if ((access.and(ACC_STATIC)) != 0) {
            methodTypes.add(MethodType.STATIC)
        }
        if ((access.and(ACC_PRIVATE)) != 0) {
            methodTypes.add(MethodType.PRIVATE)
        }
        // TODO: support more type https://asm.ow2.io/javadoc/constant-values.html#org.objectweb.asm.Opcodes
        return methodTypes
    }

}
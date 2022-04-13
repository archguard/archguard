package com.thoughtworks.archguard.metrics.domain.abstracts

import com.thoughtworks.archguard.code.clazz.domain.JClass
import com.thoughtworks.archguard.code.module.domain.model.JClassVO

class ClassAbstractRatio(val ratio: Double, val jClass: JClassVO) {
    companion object {
        fun fromJClass(jClass: JClass): ClassAbstractRatio {
            if (!jClass.isAbstractClass()) {
                return ClassAbstractRatio(0.0, jClass.toVO())
            }
            val jClassVO = jClass.toVO()
            val abstractRatio = jClass.methods.filter { it.isAbstract() }.size.toDouble() / jClass.methods.size
            return ClassAbstractRatio(abstractRatio, jClassVO)
        }
    }
}

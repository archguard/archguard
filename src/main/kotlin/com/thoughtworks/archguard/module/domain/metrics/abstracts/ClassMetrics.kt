package com.thoughtworks.archguard.module.domain.metrics.abstracts

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.module.domain.model.JClassVO

class ClassMetrics(val ratio: Double, val jClass: JClassVO) {
    companion object {
        fun fromJClass(jClass: JClass): ClassMetrics {
            if (!jClass.isAbstractClass()) {
                return ClassMetrics(0.0, jClass.toVO())
            }
            val jClassVO = jClass.toVO()
            val abstractRatio = jClass.methods.map { it.isAbstract() }.size.toDouble() / jClass.methods.size
            return ClassMetrics(abstractRatio, jClassVO)
        }
    }
}

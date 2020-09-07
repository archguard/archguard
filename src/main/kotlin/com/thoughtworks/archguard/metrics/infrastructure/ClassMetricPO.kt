package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.ClassDit
import com.thoughtworks.archguard.module.domain.model.JClassVO

class ClassDitPO(val systemId: String, val name: String, val module: String, val dit: Int) {
    fun toClassDit(): ClassDit {
        return ClassDit(JClassVO(name, module), dit)
    }
}
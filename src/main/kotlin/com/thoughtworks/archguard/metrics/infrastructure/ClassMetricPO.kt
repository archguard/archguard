package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.ClassDit
import com.thoughtworks.archguard.metrics.domain.ClassLCOM4
import com.thoughtworks.archguard.module.domain.model.JClassVO

class ClassLCOM4PO(val systemId: String, val name: String, val module: String, val lcom4: Int) {
    fun toClassLCOM4(): ClassLCOM4 {
        return ClassLCOM4(JClassVO(name, module), lcom4)
    }
}

class ClassDitPO(val systemId: String, val name: String, val module: String, val dit: Int) {
    fun toClassDit(): ClassDit {
        return ClassDit(JClassVO(name, module), dit)
    }
}
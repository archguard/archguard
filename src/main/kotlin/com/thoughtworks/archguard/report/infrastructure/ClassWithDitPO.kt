package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.deepinheritance.DeepInheritance

class ClassWithDitPO(val id: String, val systemId: Long, val name: String, val module: String, val dit: Int) {
    fun toDeepInheritance(): DeepInheritance {
        return DeepInheritance(id, systemId, module, name, dit)
    }
}
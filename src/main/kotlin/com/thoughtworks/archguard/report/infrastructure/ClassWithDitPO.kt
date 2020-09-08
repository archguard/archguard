package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.deepinheritance.DeepInheritance

class ClassWithDitPO(val id: String, val systemId: Long, val name: String, val module: String, val dit: Int) {

    private val DELIMITER = "$"
    fun toDeepInheritance(): DeepInheritance {
        return if (name.contains(DELIMITER)) {
            val list = name.split(DELIMITER).toList()
            DeepInheritance(id, systemId, module, list[0], list[1], dit)
        } else {
            DeepInheritance(id, systemId, module, "-", name, dit)
        }
    }
}
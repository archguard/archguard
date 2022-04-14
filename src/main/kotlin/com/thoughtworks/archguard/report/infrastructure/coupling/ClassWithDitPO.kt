package com.thoughtworks.archguard.report.infrastructure.coupling

import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritance

class ClassWithDitPO(val id: String, val systemId: Long, val name: String, val module: String, val dit: Int) {

    fun toDeepInheritance(): DeepInheritance {
        val lastDelimiterIndex = name.lastIndexOf(".")
        return if (lastDelimiterIndex > 0) {
            DeepInheritance(
                id, systemId, module, name.substring(0, lastDelimiterIndex),
                name.substring(lastDelimiterIndex + 1), dit
            )
        } else {
            DeepInheritance(id, systemId, module, "", name, dit)
        }
    }
}

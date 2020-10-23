package com.thoughtworks.archguard.report.infrastructure.coupling

import com.thoughtworks.archguard.report.domain.coupling.dataclumps.ClassDataClump

class ClassWithLCOM4PO(val id: String, val systemId: Long, val name: String, val module: String, val lcom4: Int) {

    fun toClassDataClump(): ClassDataClump {
        val lastDelimiterIndex = name.lastIndexOf(".")
        return if (lastDelimiterIndex > 0) {
            ClassDataClump(id, systemId, module, name.substring(0, lastDelimiterIndex),
                    name.substring(lastDelimiterIndex + 1), lcom4)
        } else {
            ClassDataClump(id, systemId, module, "", name, lcom4)
        }
    }
}
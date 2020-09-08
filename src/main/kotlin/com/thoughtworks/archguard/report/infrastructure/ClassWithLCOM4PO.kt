package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.dataclumps.ClassDataClump

class ClassWithLCOM4PO(val id: String, val systemId: Long, val name: String, val module: String, val lcom4: Int) {
    private val DELIMITER = "$"
    fun toClassDataClump(): ClassDataClump {
        return if (name.contains(DELIMITER)) {
            val list = name.split(DELIMITER).toList()
            ClassDataClump(id, systemId, module, list[0], list[1], lcom4)
        } else {
            ClassDataClump(id, systemId, module, "-", name, lcom4)
        }
    }
}
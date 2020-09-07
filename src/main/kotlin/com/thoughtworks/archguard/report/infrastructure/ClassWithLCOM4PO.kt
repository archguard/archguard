package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.dataclumps.ClassDataClump

class ClassWithLCOM4PO(val id: String, val systemId: Long, val name: String, val module: String, val lcom4: Int) {
    fun toClassDataClump(): ClassDataClump {
        return ClassDataClump(id, systemId, module, name, lcom4)
    }
}
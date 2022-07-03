package com.thoughtworks.archguard.v2.backyard.persistent.po

import com.thoughtworks.archguard.common.TypeMap
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClass

data class JClassPo(val id: String, val name: String, val module: String?, val loc: Int?, val access: String?) {
    companion object {
        fun JClassPo.toJClass(): JClass {
            val jClass = JClass(id, name, module)
            access?.toIntOrNull()
                ?.let { TypeMap.getClassType(it) }
                ?.forEach { jClass.addClassType(it) }
            return jClass
        }
    }
}
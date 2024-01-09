package org.archguard.model.vos

import com.fasterxml.jackson.annotation.JsonIgnore
import org.archguard.arch.LogicComponent
import org.archguard.arch.LogicModuleMemberType
import org.archguard.graph.Node
import org.archguard.model.code.JClass

/**
 * JClassVO is a Value Object, use for LogicModule aggregation
 */
data class JClassVO(val name: String, val module: String?) : LogicComponent(), Node {
    var id: String? = null

    @JsonIgnore
    override fun getNodeId(): String {
        return id!!
    }

    fun getPackageName(): String {
        if (!name.contains('.')) return ""

        val endIndex = name.indexOfLast { it == '.' }
        return name.substring(0, endIndex)
    }

    fun getTypeName(): String {
        return name.substringAfterLast(".")
    }

    fun getBaseClassName(): String {
        return name.substringBefore("$", name)
    }


    companion object {
        fun create(fullName: String): JClassVO {
            val split = fullName.split(".")
            return JClassVO(split.subList(1, split.size).joinToString(".").trim(), split[0].trim())
        }

        fun fromClass(jClass: JClass): JClassVO {
            val jClassVO = JClassVO(jClass.name, jClass.module)
            jClassVO.id = jClass.id
            return jClassVO
        }
    }


    override fun containsOrEquals(logicComponent: LogicComponent): Boolean {
        return logicComponent.getType() == LogicModuleMemberType.CLASS && logicComponent.getFullName() == this.getFullName()
    }

    override fun getFullName(): String {
        return "$module.$name"
    }

    override fun getType(): LogicModuleMemberType {
        return LogicModuleMemberType.CLASS
    }
}

package org.archguard.model.vos

import com.fasterxml.jackson.annotation.JsonIgnore
import org.archguard.graph.Node
import org.archguard.model.code.JMethod

data class JMethodVO(val name: String, val clazz: JClassVO, val returnType: String, val argumentTypes: List<String>) :
    Node {
    var id: String? = null
    val fullName = clazz.fullName + "." + name + "(" + argumentTypes.joinToString(separator = ",") + ")"

    constructor(
        name: String,
        className: String,
        moduleName: String?,
        returnType: String,
        argumentTypes: List<String>
    ) : this(name, JClassVO(className, moduleName), returnType, argumentTypes)

    @JsonIgnore
    override fun getNodeId(): String {
        return id!!
    }

    companion object {
        fun fromJMethod(jMethod: JMethod): JMethodVO {
            val jMethodVO = JMethodVO(
                jMethod.name,
                jMethod.clazz,
                jMethod.module,
                jMethod.returnType,
                jMethod.argumentTypes
            )
            jMethodVO.id = jMethod.id
            return jMethodVO
        }
    }
}

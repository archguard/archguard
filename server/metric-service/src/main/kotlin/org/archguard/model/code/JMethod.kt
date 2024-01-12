package org.archguard.model.code

import kotlinx.serialization.Serializable
import org.archguard.config.ConfigType
import org.archguard.config.Configure
import org.archguard.model.vos.JMethodVO

@Serializable
data class JMethod(
    val id: String,
    val name: String,
    val clazz: String,
    val module: String?,
    val returnType: String,
    val argumentTypes: List<String>
) {
    var callees: List<JMethod> = ArrayList()
    var callers: List<JMethod> = ArrayList()
    var parents: List<JMethod> = ArrayList()
    var implements: List<JMethod> = ArrayList()
    val methodTypes: MutableList<MethodType> = mutableListOf()
    var fields: List<JField> = ArrayList()

    val configuresMap: MutableMap<String, String> = mutableMapOf()

    fun addType(methodType: MethodType) {
        this.methodTypes.add(methodType)
    }

    fun isAbstract(): Boolean {
        return methodTypes.contains(MethodType.ABSTRACT_METHOD)
    }

    fun isSynthetic(): Boolean {
        return methodTypes.contains(MethodType.SYNTHETIC)
    }

    fun isStatic(): Boolean {
        return methodTypes.contains(MethodType.STATIC)
    }

    fun isPrivate(): Boolean {
        return methodTypes.contains(MethodType.PRIVATE)
    }

    fun toVO(): JMethodVO {
        val jMethodVO = JMethodVO(name, clazz, module, returnType, argumentTypes)
        jMethodVO.id = id
        return jMethodVO
    }

    fun buildColorConfigure(configures: List<Configure>) {
        var highestOrder = 0
        for (configure in configures.filter { it.type == ConfigType.COLOR.typeName }) {
            val order = configure.order
            if (order <= highestOrder) {
                continue
            }
            highestOrder = order
            val color = configure.value
            configuresMap[ConfigType.COLOR.typeName] = color
        }
    }
}


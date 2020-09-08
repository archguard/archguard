package com.thoughtworks.archgard.scanner2.domain.model


class JMethod(val id: String, val name: String, val clazz: String, val module: String, val returnType: String, val argumentTypes: List<String>) {
    var callees: List<JMethod> = ArrayList()
    val methodTypes: MutableList<MethodType> = mutableListOf()
    var fields: List<JField> = ArrayList()

    fun addType(methodType: MethodType) {
        this.methodTypes.add(methodType)
    }

    fun toVO(): JMethodVO {
        val jMethodVO = JMethodVO(name, clazz, module, returnType, argumentTypes)
        jMethodVO.id = id
        return jMethodVO
    }
}

// 暂时只有接口和类
enum class MethodType {
    NOT_DEFINED, ABSTRACT_METHOD
}
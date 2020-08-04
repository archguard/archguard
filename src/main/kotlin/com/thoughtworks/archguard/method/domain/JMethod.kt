package com.thoughtworks.archguard.method.domain

class JMethod(val id: String, val name: String, val clazz: String, val module: String, val returnType: String?, val argumentTypes: List<String>) {
    var callees: List<JMethod> = ArrayList()
    var callers: List<JMethod> = ArrayList()
    var parents: List<JMethod> = ArrayList()
    var implements: List<JMethod> = ArrayList()
    private val methodTypes: MutableList<MethodType> = mutableListOf()

    fun addType(methodType: MethodType) {
        this.methodTypes.add(methodType)
    }

    fun isAbstract(): Boolean {
        return methodTypes.contains(MethodType.ABSTRACT_METHOD)
    }
}

// 暂时只有接口和类
enum class MethodType {
    NOT_DEFINED, ABSTRACT_METHOD
}
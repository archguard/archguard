package org.archguard.model.code

class JMethod(val id: String, val name: String, val clazz: String, val module: String?, val returnType: String, val argumentTypes: List<String>) {
    var callees: List<JMethod> = ArrayList()
    val methodTypes: MutableList<MethodType> = mutableListOf()
    var fields: List<JField> = ArrayList()

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
}

// 暂时只有接口和类
enum class MethodType {
    NOT_DEFINED, ABSTRACT_METHOD, SYNTHETIC, STATIC, PRIVATE
}

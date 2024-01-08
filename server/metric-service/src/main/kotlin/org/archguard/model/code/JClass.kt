package org.archguard.model.code

/**
 * JClass is an Entity, so it must have an id.
 */
data class JClass(val id: String, val name: String, val module: String?) {
    var methods: List<JMethod> = ArrayList()
    var parents: List<JClass> = ArrayList()
    var implements: List<JClass> = ArrayList()
    var fields: List<JField> = ArrayList()

    private val classType: MutableList<ClazzType> = mutableListOf()

    fun addClassType(clazzType: ClazzType) {
        classType.add(clazzType)
    }

    fun isInterface(): Boolean {
        return classType.contains(ClazzType.INTERFACE)
    }

    fun isAbstract(): Boolean {
        return classType.contains(ClazzType.ABSTRACT_CLASS)
    }

    fun isSynthetic(): Boolean {
        return classType.contains(ClazzType.SYNTHETIC)
    }
}

// 暂时只有接口和类
enum class ClazzType {
    INTERFACE, CLASS, NOT_DEFINED, ABSTRACT_CLASS, SYNTHETIC
}

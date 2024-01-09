package org.archguard.model.code

import org.archguard.config.ConfigType
import org.archguard.config.Configure
import org.archguard.model.vos.JClassVO

/**
 * JClass is an Entity, so it must have an id.
 */
data class JClass(val id: String, val name: String, val module: String?) {
    var methods: List<JMethod> = ArrayList()
    var callees: List<ClassRelation> = ArrayList()
    var callers: List<ClassRelation> = ArrayList()
    var parents: List<JClass> = ArrayList()
    var implements: List<JClass> = ArrayList()
    var dependencies: List<JClass> = ArrayList()
    var dependencers: List<JClass> = ArrayList()
    var fields: List<JField> = ArrayList()

    private val classType: MutableList<ClazzType> = mutableListOf()
    val configuresMap: MutableMap<String, String> = mutableMapOf()

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

    fun isAbstractClass(): Boolean {
        return classType.contains(ClazzType.ABSTRACT_CLASS)
    }

    fun getFullName(): String {
        return "$module.$name"
    }

    fun toVO(): JClassVO {
        val jClassVO = JClassVO(name, module)
        jClassVO.id = this.id
        return jClassVO
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

package com.thoughtworks.archgard.scanner2.domain.model

import org.slf4j.LoggerFactory

/**
 * JClass is an Entity, so it must have an id.
 */
open class JClass(val id: String, val name: String, val module: String) {
    private val log = LoggerFactory.getLogger(JClass::class.java)

    var methods: List<JMethod> = ArrayList()
    var callees: List<ClassRelation> = ArrayList()
    var callers: List<ClassRelation> = ArrayList()
    var parents: List<JClass> = ArrayList()
    var implements: List<JClass> = ArrayList()
    var dependencees: List<JClass> = ArrayList()
    var dependencers: List<JClass> = ArrayList()
    var fields: List<JField> = ArrayList()
    private val classType: MutableList<ClazzType> = mutableListOf()

    fun addClassType(clazzType: ClazzType) {
        classType.add(clazzType)
    }

    override fun toString(): String {
        return "JClass(name='$name', module='$module', callees=$callees, callers=$callers, parents=$parents, implements=$implements, dependencees=$dependencees, dependencers=$dependencers, id=$id, classType=$classType)"
    }

    fun getFullName(): String {
        return "$module.$name"
    }

    fun toVO(): JClassVO {
        val jClassVO = JClassVO(name, module)
        jClassVO.id = this.id
        return jClassVO
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JClass

        if (id != other.id) return false
        if (name != other.name) return false
        if (module != other.module) return false
        if (classType != other.classType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + module.hashCode()
        result = 31 * result + classType.hashCode()
        return result
    }
}

// 暂时只有接口和类
enum class ClazzType {
    INTERFACE, CLASS, NOT_DEFINED, ABSTRACT_CLASS
}

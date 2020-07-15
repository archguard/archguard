package com.thoughtworks.archguard.module.domain.model

import com.thoughtworks.archguard.clazz.domain.ClassRelation
import org.slf4j.LoggerFactory

class JClass(val id: String, val name: String, val module: String) {
    private val log = LoggerFactory.getLogger(JClass::class.java)

    var callees: List<ClassRelation> = ArrayList()
    var callers: List<ClassRelation> = ArrayList()
    var parents: List<JClass> = ArrayList()
    var implements: List<JClass> = ArrayList()
    var dependencees: List<JClass> = ArrayList()
    var dependencers: List<JClass> = ArrayList()
    var classType: ClazzType = ClazzType.NOT_DEFINED

    fun isInterface(): Boolean {
        return classType == ClazzType.INTERFACE
    }


    override fun toString(): String {
        return "JClass(name='$name', module='$module', callees=$callees, callers=$callers, parents=$parents, implements=$implements, dependencees=$dependencees, dependencers=$dependencers, id=$id, classType=$classType)"
    }

    fun getFullName(): String {
        return "$module.$name"
    }

    fun toVO(): JClassVO {
        return JClassVO(name, module)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JClass

        if (name != other.name) return false
        if (module != other.module) return false
        if (id != other.id) return false
        if (classType != other.classType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + module.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + classType.hashCode()
        return result
    }
}

// 暂时只有接口和类
enum class ClazzType {
    INTERFACE, CLASS, NOT_DEFINED
}

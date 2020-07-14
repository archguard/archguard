package com.thoughtworks.archguard.clazz.domain

import com.thoughtworks.archguard.module.domain.model.JClass

data class ClassRelation(val clazz: JClass, val count: Int) {

    override fun toString(): String {
        return "ClassRelation(clazz=${clazz.toString()}, count=$count)"
    }
}



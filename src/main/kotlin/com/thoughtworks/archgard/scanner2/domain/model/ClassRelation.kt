package com.thoughtworks.archgard.scanner2.domain.model

data class ClassRelation(val clazz: JClass, val count: Int) {

    override fun toString(): String {
        return "ClassRelation(clazz=${clazz.toString()}, count=$count)"
    }
}



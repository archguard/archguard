package com.thoughtworks.archguard.clazz.domain

data class ClassRelation(val clazz: JClass, val count: Int) {

    override fun toString(): String {
        return "ClassRelation(clazz=$clazz, count=$count)"
    }
}



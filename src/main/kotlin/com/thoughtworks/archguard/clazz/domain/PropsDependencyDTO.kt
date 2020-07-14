package com.thoughtworks.archguard.clazz.domain

class PropsDependencyDTO(val a: String, val b: String, val count: Int) {
    fun toPropsDependency(props: Map<String, Boolean>): PropsDependency<String> {
        return PropsDependency(a, b, count, props)
    }
}

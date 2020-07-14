package com.thoughtworks.archguard.clazz.domain

class PropsDependencyDTO(val caller: String, val callee: String, val count: Int) {
    fun toPropsDependency(props: Map<String, Boolean>): PropsDependency<String> {
        return PropsDependency(caller, callee, count, props)
    }
}

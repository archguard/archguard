package com.thoughtworks.archguard.scanner2.domain.model

class MethodAccess(val id: String, val isSynthetic: Boolean, val isAbstract: Boolean,
                   val isStatic: Boolean, val isPrivate: Boolean) {

    companion object {
        fun from(methods: List<JMethod>): List<MethodAccess> {
            return methods.map { MethodAccess(it.id, it.isSynthetic(), it.isAbstract(), it.isStatic(), it.isPrivate()) }
        }
    }
}
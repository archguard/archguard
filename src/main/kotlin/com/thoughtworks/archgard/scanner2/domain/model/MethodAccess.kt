package com.thoughtworks.archgard.scanner2.domain.model

class MethodAccess(val id: String, val isSynthetic: Boolean, val isAbstract: Boolean) {

    companion object {
        fun from(methods: List<JMethod>): List<MethodAccess> {
            return methods.map { MethodAccess(it.id, it.isSynthetic(), it.isAbstract()) }
        }
    }
}
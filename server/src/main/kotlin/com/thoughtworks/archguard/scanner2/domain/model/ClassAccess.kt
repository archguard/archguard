package com.thoughtworks.archguard.scanner2.domain.model

class ClassAccess(val id: String, val isInterface: Boolean, val isAbstract: Boolean, val isSynthetic: Boolean) {

    companion object {
        fun from(classes: List<JClass>): List<ClassAccess> {
            return classes.map { ClassAccess(it.id, it.isInterface(), it.isAbstract(), it.isSynthetic()) }
        }
    }
}

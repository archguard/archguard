package com.thoughtworks.archguard.code.module.domain.model

import org.archguard.arch.LogicComponent
import org.archguard.model.vos.JClassVO

object LeafManger {
    fun createLeaf(name: String): LogicComponent {
        if (name.split(".").size > 1) {
            return JClassVO.create(name)
        }

        return SubModule(name)
    }
}

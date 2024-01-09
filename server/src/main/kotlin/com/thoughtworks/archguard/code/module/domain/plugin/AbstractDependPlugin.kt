package com.thoughtworks.archguard.code.module.domain.plugin

import com.thoughtworks.archguard.code.module.domain.model.JMethodVO
import org.archguard.model.Dependency

abstract class AbstractDependPlugin : DependPlugin {

    override fun fixMethodDependencies(systemId: Long, methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>> {
        return methodDependencies
    }
}

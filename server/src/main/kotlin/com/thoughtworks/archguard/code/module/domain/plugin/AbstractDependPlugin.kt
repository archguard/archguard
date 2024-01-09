package com.thoughtworks.archguard.code.module.domain.plugin

import org.archguard.model.Dependency
import org.archguard.model.vos.JMethodVO

abstract class AbstractDependPlugin : DependPlugin {

    override fun fixMethodDependencies(systemId: Long, methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>> {
        return methodDependencies
    }
}

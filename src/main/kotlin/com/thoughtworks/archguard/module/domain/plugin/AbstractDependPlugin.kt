package com.thoughtworks.archguard.module.domain.plugin

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethodVO

abstract class AbstractDependPlugin : DependPlugin {

    override fun fixMethodDependencies(systemId: Long, methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>>{
        return methodDependencies
    }
}

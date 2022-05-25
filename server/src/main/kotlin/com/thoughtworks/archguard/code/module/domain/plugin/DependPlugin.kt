package com.thoughtworks.archguard.code.module.domain.plugin

import com.thoughtworks.archguard.code.module.domain.model.Dependency
import com.thoughtworks.archguard.code.module.domain.model.JMethodVO

interface DependPlugin : Plugin {

    fun fixMethodDependencies(systemId: Long, methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>>
}

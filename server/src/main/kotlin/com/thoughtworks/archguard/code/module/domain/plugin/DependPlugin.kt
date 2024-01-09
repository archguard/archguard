package com.thoughtworks.archguard.code.module.domain.plugin

import com.thoughtworks.archguard.code.module.domain.model.JMethodVO
import org.archguard.model.Dependency

interface DependPlugin : Plugin {

    fun fixMethodDependencies(systemId: Long, methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>>
}

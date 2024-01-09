package com.thoughtworks.archguard.code.module.domain.plugin

import org.archguard.model.Dependency
import org.archguard.model.vos.JMethodVO
import org.archguard.plugin.Plugin

interface DependPlugin : Plugin {

    fun fixMethodDependencies(systemId: Long, methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>>
}

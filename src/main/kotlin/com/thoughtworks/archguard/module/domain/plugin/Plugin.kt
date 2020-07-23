package com.thoughtworks.archguard.module.domain.plugin

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicModule

abstract class Plugin {
    open fun mapToModuleDependency(methodDependency: Dependency<JMethodVO>, logicModules: List<LogicModule>, logicModuleDependencies: List<Dependency<LogicModule>>): List<Dependency<LogicModule>> {
        return logicModuleDependencies
    }
}

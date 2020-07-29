package com.thoughtworks.archguard.module.domain.plugin

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicModule

interface DependPlugin : Plugin {
    fun mapToModuleDependencies(dependencies: List<Dependency<JClassVO>>,
                                logicModules: List<LogicModule>,
                                logicModuleDependencies: List<Dependency<LogicModule>>)
            : List<Dependency<LogicModule>>

    fun fixMethodDependencies(methodDependencies: List<Dependency<JMethodVO>>)
            : List<Dependency<JMethodVO>>
}
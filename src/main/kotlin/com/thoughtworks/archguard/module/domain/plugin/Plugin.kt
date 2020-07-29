package com.thoughtworks.archguard.module.domain.plugin

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicModule

abstract class Plugin {
    open fun mapToModuleDependencies(dependencies: List<Dependency<JClassVO>>, logicModules: List<LogicModule>, logicModuleDependencies: List<Dependency<LogicModule>>): List<Dependency<LogicModule>> {
        return logicModuleDependencies
    }

    open fun fixMethodDependencies(methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>>{
        return methodDependencies
    }

    open fun fixClassDependencies(classDependencies: List<Dependency<JClassVO>>): List<Dependency<JClassVO>>{
        return classDependencies
    }
}

package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicModule

interface DependencyAnalysisHelper {
    fun analysis(classDependency: Dependency<JClass>, logicModules: List<LogicModule>): List<LogicModule>
}
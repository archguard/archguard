package com.thoughtworks.archguard.module.domain

interface DependencyAnalysisHelper {
    fun analysis(classDependency: Dependency<JClass>, logicModules: List<LogicModule>, calleeModules: List<LogicModule>): List<LogicModule>
}
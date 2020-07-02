package com.thoughtworks.archguard.module.domain

interface DependencyAnalysisHelper {
    fun analysis(classDependency: Dependency, logicModules: List<LogicModule>, calleeModules: List<String>): List<String>
}
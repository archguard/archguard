package com.thoughtworks.archguard.module.domain

interface DependencyAnalysisHelper {
    fun analysis(classDependency: NewDependency<JClass>, logicModules: List<NewLogicModule>, calleeModules: List<NewLogicModule>): List<NewLogicModule>
}
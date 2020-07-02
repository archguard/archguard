package com.thoughtworks.archguard.module.domain

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
@Qualifier("Default")
class DefaultDependencyAnalysisHelper : DependencyAnalysisHelper {
    override fun analysis(classDependency: Dependency, logicModules: List<LogicModule>, calleeModules: List<String>): List<String> {
        return calleeModules
    }
}
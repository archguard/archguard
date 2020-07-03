package com.thoughtworks.archguard.module.domain

interface DependencyService {
    fun getLogicModulesDependencies(caller: String, callee: String): List<Dependency<JMethod>>
}